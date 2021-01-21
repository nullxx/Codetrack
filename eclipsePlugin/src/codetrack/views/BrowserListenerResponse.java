package codetrack.views;

import com.google.gson.JsonObject;

public class BrowserListenerResponse {
	private BrowserListenerResponseType type;
	private JsonObject data;

	public BrowserListenerResponse(String type, JsonObject data) {
		this.setData(data);
		this.setType(BrowserListenerResponseType.valueOf(type.toUpperCase()));
	}

	public BrowserListenerResponse(BrowserListenerResponseType type, JsonObject data) {
		this.setData(data);
		this.setType(type);
	}

	public JsonObject getData() {
		return data;
	}

	public void setData(JsonObject data) {
		this.data = data;
	}

	public BrowserListenerResponseType getType() {
		return type;
	}

	public void setType(BrowserListenerResponseType type) {
		this.type = type;
	}
}
