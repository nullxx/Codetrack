package codetrack.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Observation {
	private int id;
	private int group;
	private String shortId;
	private String createdAt;
	private String updatedAt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public String getShortId() {
		return shortId;
	}

	public void setShortId(String shortId) {
		this.shortId = shortId;
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

	public static String[] getData(Observation[] observations) {
		String[] result = new String[observations.length];

		for (int i = 0; i < observations.length; i++) {
			result[i] = String.format("%s %s %s", Integer.toString(observations[i].getId()),
					observations[i].getShortId(),
					observations[i].getCreatedAt() == null ? "" : observations[i].getCreatedAt());
		}
		return result;
	}

	/**
	 * Format a Observation in JSON
	 * 
	 * @return JsonObject
	 */
	public JsonObject formatJSON() {
		JsonObject json = new JsonObject();
		json.addProperty("id", this.getId());
		json.addProperty("group", this.getGroup());
		json.addProperty("shortId", this.getShortId());
		json.addProperty("createdAt", this.getCreatedAt());
		json.addProperty("updatedAt", this.getUpdatedAt());
		return json;
	}

	/**
	 * Format a Observation[] in JSON
	 * 
	 * @return JsonArray
	 */

	public static JsonArray observationsJSON(Observation[] observations) {
		JsonArray jsonArray = new JsonArray();
		for (int i = 0; i < observations.length; i++) {
			jsonArray.add(observations[i].formatJSON());
		}
		return jsonArray;
	}

	public String toString() {
		return String.format("Observation %s - shortId %s", this.getId(), this.getShortId());
	}

}
