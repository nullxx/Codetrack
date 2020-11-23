package codetrack.api;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import codetrack.Config;
import codetrack.Storage;

public class HttpCaller {
	private final static HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

	/**
	 * Middleware to check if error on HTTP response
	 * @param response
	 * @throws JsonIOException
	 * @throws IOException
	 */
	private static void processAuthResponse(JsonObject response) throws JsonIOException, IOException {
		JsonElement data = response.get(Config.REMOTE_RESPONSE_KEY_DATA);
		JsonElement error = response.get(Config.REMOTE_RESPONSE_KEY_ERROR); // TODO
		if (error != null && !error.isJsonNull()) {
			System.err.println(error);
		} else if (data != null && !data.isJsonNull()) {
			if (data.getAsJsonObject().get(Config.REMOTE_RESPONSE_KEY_TOKEN) != null) {
				Storage storage = new Storage();
				storage.saveProp(Config.LOCAL_STORAGE_KEY_BEARER_TOKEN, data.getAsJsonObject().get(Config.REMOTE_RESPONSE_KEY_TOKEN).getAsString());
			}
		}
	}
	
	/**
	 * Get the stoed auth token
	 * @return String
	 * @throws JsonIOException
	 * @throws IOException
	 */
	private static String getAuthToken() throws JsonIOException, IOException {
		Storage storage = new Storage();
		return storage.getString(Config.LOCAL_STORAGE_KEY_BEARER_TOKEN);
	}

	/**
	 * Make a POST request
	 * @param url
	 * @param data
	 * @return JsonObject
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static JsonObject POST(String url, HashMap<String, Object> data) throws IOException, InterruptedException {
		String boundary = new BigInteger(256, new Random()).toString();
		HttpRequest.BodyPublisher bodyPublisher = buildFormDataFromMap(data, boundary);
		HttpRequest request = HttpRequest.newBuilder().POST(bodyPublisher).uri(URI.create(url))
				.header("Content-Type", "multipart/form-data;boundary=" + boundary)
				.header("Authorization", String.format(Config.HTTP_HEADER_BEARER_TOKEN, HttpCaller.getAuthToken())).build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		JsonObject res = (JsonObject) new JsonParser().parse(response.body());
		HttpCaller.processAuthResponse(res);
		return res;

	}
	
	/**
	 * Make a PUT request
	 * @param url
	 * @param data
	 * @return JsonObject
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static JsonObject PUT(String url, HashMap<String, Object> data) throws IOException, InterruptedException {
		String boundary = new BigInteger(256, new Random()).toString();
		HttpRequest.BodyPublisher bodyPublisher = buildFormDataFromMap(data, boundary);
		HttpRequest request = HttpRequest.newBuilder().PUT(bodyPublisher).uri(URI.create(url))
				.header("Content-Type", "multipart/form-data;boundary=" + boundary)
				.header("Authorization", String.format(Config.HTTP_HEADER_BEARER_TOKEN, HttpCaller.getAuthToken())).build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		JsonObject res = (JsonObject) new JsonParser().parse(response.body());
		return res;

	}
	
	/**
	 * Make a GET request
	 * @param url
	 * @return JsonObject
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static JsonObject GET(String url) throws IOException, InterruptedException {

		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url))
				.header("Authorization", String.format(Config.HTTP_HEADER_BEARER_TOKEN, HttpCaller.getAuthToken())).build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		JsonObject res = (JsonObject) new JsonParser().parse(response.body());

		return res;

	}
	
	/**
	 * Create the body of the request
	 * @param data
	 * @param boundary
	 * @return HttpRequest.BodyPublisher
	 * @throws IOException
	 */
	private static HttpRequest.BodyPublisher buildFormDataFromMap(HashMap<String, Object> data, String boundary)
			throws IOException {
		// Result request body
		List<byte[]> byteArrays = new ArrayList<>();

		byte[] separator = ("--" + boundary + "\r\nContent-Disposition: form-data; name=")
				.getBytes(Config.HTTP_CHARSET);

		for (HashMap.Entry<String, Object> entry : data.entrySet()) {

			byteArrays.add(separator);

			if (entry.getValue() instanceof Path) {
				var path = (Path) entry.getValue();
				String mimeType = Files.probeContentType(path);
				byteArrays.add(("\"" + entry.getKey() + "\"; filename=\"" + path.getFileName() + "\"\r\nContent-Type: "
						+ mimeType + "\r\n\r\n").getBytes(Config.HTTP_CHARSET));
				byteArrays.add(Files.readAllBytes(path));
				byteArrays.add("\r\n".getBytes(Config.HTTP_CHARSET));
			} else {
				byteArrays.add(("\"" + entry.getKey() + "\"\r\n\r\n" + entry.getValue() + "\r\n")
						.getBytes(Config.HTTP_CHARSET));
			}
		}

		byteArrays.add(("--" + boundary + "--").getBytes(Config.HTTP_CHARSET));

		return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
	}
}
