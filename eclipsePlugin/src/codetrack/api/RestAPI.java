package codetrack.api;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import codetrack.Config;
import codetrack.client.Group;
import codetrack.client.Observation;
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

	/**
	 * [USER/ADMIN] Login a user to remote
	 * 
	 * @param email
	 * @param password
	 * @return boolean success
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static User login(String email, String password) throws IOException, InterruptedException {
		User user = new User(email, password);

		JsonObject response = HttpCaller.POST(String.format("%s/%s", baseURL, "login"), user.getFormated());
		if (RestAPI.isCorrect(response)) {
			System.out.println(response);
			boolean admin = response.get("data").getAsJsonObject().get("admin").getAsBoolean();
			user.setAdmin(admin);

			return user;
		}

		return null;
	}

	/**
	 * [USER] Get allowed projects from remote
	 * 
	 * @return RemoteProject[]
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static Object getAllowedProjects(boolean raw) throws IOException, InterruptedException {
		JsonObject response = HttpCaller.GET(String.format("%s/%s", baseURL, "project"));
		if (RestAPI.isCorrect(response)) {
			JsonArray projectList = response.get(Config.REMOTE_RESPONSE_KEY_DATA).getAsJsonArray();
			if (raw) {
				return projectList;
			}
			RemoteProject[] projects = new RemoteProject[projectList.size()];
			for (int i = 0; i < projectList.size(); i++) {
				JsonObject project = projectList.get(i).getAsJsonObject();
				RemoteProject temp = new RemoteProject();
				temp.setId(project.get("id").getAsInt());
				temp.setName(project.get("name").getAsString());
				temp.setUser(project.get("user").getAsString());
				temp.setProjectNatures(project.get("natures").getAsString());
				temp.setAllowed(project.get("isAllowed").getAsBoolean());
				temp.setCreatedAt(project.get("createdAt").getAsString());
				temp.setUpdatedAt(!project.get("updatedAt").isJsonNull() ? project.get("updatedAt").getAsString() : "");
				projects[i] = temp;
			}
			return projects;
		} else {
			throw new IOException(String.format("Error from remote: %s", response.toString()));
		}
	}

	/**
	 * [USER] Get project from its id
	 * 
	 * @return RemoteProject
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static RemoteProject getProject(int projectId) throws IOException, InterruptedException {
		JsonObject response = HttpCaller.GET(String.format("%s/%s/%d", baseURL, "project", projectId));
		if (RestAPI.isCorrect(response)) {

			JsonObject project = response.get(Config.REMOTE_RESPONSE_KEY_DATA).getAsJsonObject();
			RemoteProject temp = new RemoteProject();
			temp.setId(project.get("id").getAsInt());
			temp.setName(project.get("name").getAsString());
			temp.setUser(project.get("user").getAsString());
			temp.setProjectNatures(project.get("natures").getAsString());
			temp.setAllowed(project.get("isAllowed").getAsBoolean());
			temp.setCreatedAt(project.get("createdAt").getAsString());
			temp.setUpdatedAt(!project.get("updatedAt").isJsonNull() ? project.get("updatedAt").getAsString() : "");

			return temp;
		} else {
			throw new IOException(String.format("Error from remote: %s", response.toString()));
		}
	}

	/**
	 * [USER] Create remote project
	 * 
	 * @param initialProjectData
	 * @return RemoteProject
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
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
			project.setProjectNatures(responseData.get("natures").getAsString());

			return project;
		} else {
			throw new IOException(
					String.format("Error from remote: ", response.get(Config.REMOTE_RESPONSE_KEY_DATA).getAsString()));
		}
	}

	/**
	 * [USER] Update remoteProject details
	 * 
	 * @param updateData
	 * @return boolean success
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static boolean updateProject(HashMap<String, Object> updateData) throws IOException, InterruptedException {
		JsonObject response = HttpCaller.PUT(String.format("%s/%s", baseURL, "project"), updateData);
		System.out.println(response.toString());
		if (RestAPI.isCorrect(response)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * [USER] Create remote snapshot
	 * 
	 * @param data
	 * @return boolean success
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static boolean createSnapshot(HashMap<String, Object> data) throws IOException, InterruptedException {
		JsonObject response = HttpCaller.PUT(String.format("%s/%s", baseURL, "project/snapshot"), data);
		if (RestAPI.isCorrect(response)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * [ADMIN] get admin groups
	 * 
	 * @return Group[]
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static Group[] getGroups() throws IOException, InterruptedException {
		JsonObject response = HttpCaller.GET(String.format("%s/%s", baseURL, "admin/groups"));
		if (RestAPI.isCorrect(response)) {
			JsonArray data = response.get("data").getAsJsonArray();
			Group[] groups = new Group[data.size()];
			for (int i = 0; i < data.size(); i++) {
				JsonObject obj = data.get(i).getAsJsonObject();

				int id = obj.get("id").getAsInt();
				String name = obj.get("name").getAsString();
				String description = obj.get("description").getAsString();
				String createdAt = null;
				String updatedAt = null;
				if (!obj.get("createdAt").isJsonNull()) {
					createdAt = obj.get("createdAt").getAsString();
				}
				if (!obj.get("updatedAt").isJsonNull()) {
					updatedAt = obj.get("updatedAt").getAsString();
				}

				Group temp = new Group(id);
				temp.setName(name);
				temp.setDescription(description);
				temp.setCreatedAt(createdAt);
				temp.setUpdatedAt(updatedAt);

				groups[i] = temp;
			}
			return groups;
		}
		return null;
	}

	/**
	 * [ADMIN] Get group observations
	 * 
	 * @param rgroup
	 * @return Observation[]
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static Observation[] getObservations(int rgroup) throws IOException, InterruptedException {
		JsonObject response = HttpCaller
				.GET(String.format("%s/%s", baseURL, String.format("admin/observations/%d", rgroup)));
		if (RestAPI.isCorrect(response)) {
			JsonArray data = response.get("data").getAsJsonArray();
			Observation[] observations = new Observation[data.size()];
			for (int i = 0; i < data.size(); i++) {
				JsonObject obj = data.get(i).getAsJsonObject();

				int id = obj.get("id").getAsInt();
				int group = obj.get("group").getAsInt();
				String shortId = obj.get("shortId").getAsString();
				String createdAt = null;
				String updatedAt = null;
				if (!obj.get("createdAt").isJsonNull()) {
					createdAt = obj.get("createdAt").getAsString();
				}
				if (!obj.get("updatedAt").isJsonNull()) {
					updatedAt = obj.get("updatedAt").getAsString();
				}

				Observation temp = new Observation();
				temp.setId(id);
				temp.setGroup(group);
				temp.setShortId(shortId);
				temp.setCreatedAt(createdAt);
				temp.setUpdatedAt(updatedAt);

				observations[i] = temp;
			}
			return observations;
		}
		return null;
	}

	/**
	 * [ADMIN] Get projects from a observation
	 * 
	 * @param shortId
	 * @return RemoteProject[]
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static RemoteProject[] getObservationProjects(String shortId) throws IOException, InterruptedException {
		JsonObject response = HttpCaller
				.GET(String.format("%s/%s", baseURL, String.format("admin/observations/%s/projects", shortId)));
		if (RestAPI.isCorrect(response)) {
			JsonArray data = response.get("data").getAsJsonArray();
			RemoteProject[] projects = new RemoteProject[data.size()];
			for (int i = 0; i < data.size(); i++) {
				JsonObject obj = data.get(i).getAsJsonObject();

				int project = obj.get("project").getAsInt();
				String observationShortId = obj.get("observation").getAsString();
				String createdAt = null;
				String updatedAt = null;
				if (!obj.get("createdAt").isJsonNull()) {
					createdAt = obj.get("createdAt").getAsString();
				}
				if (!obj.get("updatedAt").isJsonNull()) {
					updatedAt = obj.get("updatedAt").getAsString();
				}

				RemoteProject temp = RemoteProject.fromId(project);

				temp.setObservationShortId(observationShortId);
				temp.setCreatedAt(createdAt);
				temp.setUpdatedAt(updatedAt);

				projects[i] = temp;
			}
			return projects;
		}
		return null;
	}

	/**
	 * [ADMIN] Download a project from its id
	 * 
	 * @param projectId
	 * @return the path where it downloads
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static Path downloadProject(int projectId) throws IOException, InterruptedException {
		Path downloadedPath = HttpCaller
				.downloadFile(String.format("%s/%s/%d/download", baseURL, "project", projectId));
		return downloadedPath;
	}
}
