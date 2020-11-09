package codetrack.api;

import java.io.IOException;

import com.google.gson.JsonObject;

import codetrack.client.User;


public class RestAPI {

	public RestAPI(String baseUrl) {
		
	}
	
	public static boolean login(String email, String password) throws IOException, InterruptedException {
		User user = new User(email, password);
		System.out.println(user.getFormated());
		JsonObject response = HttpCaller.POST("http://localhost:3000/login", user.getFormated());
		System.out.println(response.toString());
		if (response.get("code").getAsInt() == 1) {
			return true;
		}
		return false;
		
	}

}
