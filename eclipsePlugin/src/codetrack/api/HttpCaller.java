package codetrack.api;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HttpCaller {
	final static String charset = "UTF-8";
	private final static HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

	public static JsonObject POST(String url, HashMap<String, Object> data) throws IOException, InterruptedException {

		HttpRequest request = HttpRequest.newBuilder().POST(buildFormDataFromMap(data))
				.uri(URI.create(url)).header("Content-Type", "application/x-www-form-urlencoded")
				.build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		JsonObject res = (JsonObject) new JsonParser().parse(response.body());

		return res;

	}

	private static HttpRequest.BodyPublisher buildFormDataFromMap(HashMap<String, Object> data) {
		var builder = new StringBuilder();
		for (Entry<String, Object> entry : data.entrySet()) {
			if (builder.length() > 0) {
				builder.append("&");
			}
			builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
			builder.append("=");
			builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
		}
		System.out.println(builder.toString());
		return HttpRequest.BodyPublishers.ofString(builder.toString());
	}
}
