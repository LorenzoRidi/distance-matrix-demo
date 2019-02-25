package com.example.distancematrixdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication
public class DistanceMatrixApplication {

	@Value("${DISTANCE_MATRIX_API_KEY}")
	private String distanceMatrixApiKey;

	@RequestMapping("/get")
	@ResponseBody
	public String getDistance(@RequestParam(name = "from", required = true) String from,
			@RequestParam(name = "to", required = true) String to) {

		return callAndParse("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + from
				+ "&destinations=" + to + "&key=" + distanceMatrixApiKey).toString();

	}

	private JsonElement callAndParse(String endpoint) {
		URL url;
		try {
			url = new URL(endpoint);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			con.disconnect();
			return new JsonParser().parse(content.toString());
		} catch (IOException e) {
			JsonObject error = new JsonObject();
			error.add("error", new JsonPrimitive(e.getMessage()));
			return error;
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(DistanceMatrixApplication.class, args);
	}

}
