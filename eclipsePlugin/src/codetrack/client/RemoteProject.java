package codetrack.client;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import codetrack.api.RestAPI;

public class RemoteProject {

	private String name;
	private Integer id;
	private String user;
	private String createdAt;
	private String updatedAt;
	private String observationShortId;
	private boolean isAllowed;
	private String[] projectNatures;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public boolean isAllowed() {
		return isAllowed;
	}

	public void setAllowed(boolean isAllowed) {
		this.isAllowed = isAllowed;
	}

	public String getObservationShortId() {
		return observationShortId;
	}

	public void setObservationShortId(String nObservationShortId) {
		this.observationShortId = nObservationShortId;
	}

	public static RemoteProject fromId(int id) throws IOException, InterruptedException {
		return RestAPI.getProject(id);
	}

	public String[] getProjectNatures() {
		return projectNatures;
	}

	/**
	 * Set projectNatures from a comma separated String
	 * 
	 * @param projectNatures
	 */
	public void setProjectNatures(String projectNatures) {
		String[] splited = new String[0];

		if (projectNatures.length() > 0) {
			splited = projectNatures.split(",");
		}
		this.projectNatures = splited;
	}

	/**
	 * Format in JSON
	 * 
	 * @return JsonObject
	 */
	public JsonObject formatJSON() {
		JsonObject json = new JsonObject();
		json.addProperty("id", this.getId());
		json.addProperty("name", this.getName());
		json.addProperty("user", this.getUser());
		json.addProperty("isAllowed", this.isAllowed());
		json.addProperty("shortId", this.getObservationShortId());
		json.addProperty("createdAt", this.getCreatedAt());
		json.addProperty("updatedAt", this.getUpdatedAt());
		return json;
	}

	/**
	 * Format in JSON
	 * 
	 * @param projects
	 * @return JsonArray
	 */
	public static JsonArray remoteProjectsJSON(RemoteProject[] projects) {
		JsonArray jsonArray = new JsonArray();
		for (int i = 0; i < projects.length; i++) {
			jsonArray.add(projects[i].formatJSON());
		}
		return jsonArray;
	}

}
