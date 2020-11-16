package codetrack.api;

import java.io.IOException;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import codetrack.Config;
import codetrack.client.RemoteProject;
import codetrack.client.User;

public class RestAPI {
	private static String baseURL = Config.API_BASE_URL;

	// TODO if error sending data (TODO detail) save endpoint and data to send and
	// resend when network available or so on
	private static boolean isCorrect(JsonObject response) {
		if (response.get(Config.REMOTE_RESPONSE_KEY_CODE).getAsInt() == 1) {
			return true;
		}
		return false;
	}

	public static boolean login(String email, String password) throws IOException, InterruptedException {
		User user = new User(email, password);

		JsonObject response = HttpCaller.POST(String.format("%s/%s", baseURL, "login"), user.getFormated());
		return RestAPI.isCorrect(response);

	}

	public static RemoteProject[] getAllowedProjects() throws IOException, InterruptedException {
		JsonObject response = HttpCaller.GET(String.format("%s/%s", baseURL, "project"));
		if (RestAPI.isCorrect(response)) {

			JsonArray projectList = response.get(Config.REMOTE_RESPONSE_KEY_DATA).getAsJsonArray();
			RemoteProject[] projects = new RemoteProject[projectList.size()];
			for (int i = 0; i < projectList.size(); i++) {
				JsonObject project = projectList.get(i).getAsJsonObject();
				RemoteProject temp = new RemoteProject();
				temp.setId(project.get("id").getAsInt());
				temp.setName(project.get("name").getAsString());
				temp.setUser(project.get("user").getAsString());
				temp.setAllowed(project.get("isAllowed").getAsBoolean());
				temp.setCreatedAt(project.get("createdAt").getAsString());
				temp.setUpdatedAt(!project.get("updatedAt").isJsonNull() ? project.get("updatedAt").getAsString() : "");
				projects[i] = temp;
			}
			return projects;
		} else {
			throw new IOException(String.format("Error from remote: ", response.get(Config.REMOTE_RESPONSE_KEY_DATA).getAsString()));
		}
	}

	public static RemoteProject createProject(HashMap<String, Object> initialProjectData)
			throws IOException, InterruptedException {
		JsonObject response = HttpCaller.POST(String.format("%s/%s", baseURL, "project"), initialProjectData);
		if (RestAPI.isCorrect(response)) {

			JsonObject responseData = response.get(Config.REMOTE_RESPONSE_KEY_DATA).getAsJsonObject();
			RemoteProject project = new RemoteProject();

			// this is the only data returned by the server on create
			project.setId(responseData.get("id").getAsInt());
			project.setName(responseData.get("name").getAsString());
			project.setUser(responseData.get("user").getAsString());

			return project;
		} else {
			throw new IOException(String.format("Error from remote: ", response.get(Config.REMOTE_RESPONSE_KEY_DATA).getAsString()));
		}
	}

	public static boolean updateProject(HashMap<String, Object> updateData) throws IOException, InterruptedException {
		JsonObject response = HttpCaller.PUT(String.format("%s/%s", baseURL, "project"), updateData);
		System.out.println(response.toString());
		if (RestAPI.isCorrect(response)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean createSnapshot(HashMap<String, Object> data) throws IOException, InterruptedException {
		JsonObject response = HttpCaller.PUT(String.format("%s/%s", baseURL, "project/snapshot"), data);
		if (RestAPI.isCorrect(response)) {
			return true;
		} else {
			return false;
		}
	}
}
