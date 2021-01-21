package codetrack.client;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import codetrack.api.RestAPI;

public class Group {
	private int id;
	private String name;
	private String description;
	private String createdAt;
	private String updatedAt;

	public Group(int id) {
		this.setId(id);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Observation[] getObservations() throws IOException, InterruptedException {
		return RestAPI.getObservations(this.getId());
	}

	/**
	 * Format in JSON format
	 * 
	 * @return JsonObject
	 */
	public JsonObject formatJSON() {
		JsonObject json = new JsonObject();
		json.addProperty("id", this.getId());
		json.addProperty("name", this.getName());
		json.addProperty("description", this.getDescription());
		json.addProperty("createdAt", this.getCreatedAt());
		json.addProperty("updatedAt", this.getUpdatedAt());
		return json;
	}

	/**
	 * Format in JSON format
	 * 
	 * @param groups
	 * @return JsonArray
	 */
	public static JsonArray groupsJSON(Group[] groups) {
		JsonArray jsonArray = new JsonArray();
		for (int i = 0; i < groups.length; i++) {
			jsonArray.add(groups[i].formatJSON());
		}
		return jsonArray;
	}

	public String toString() {
		return String.format("%d: %s, %s", this.getId(), this.getName(), this.getDescription());
	}
}
