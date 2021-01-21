package codetrack;

import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Storage {

	public Storage() throws IOException {
		if (!Utils.fileExists(this.getRootPath())) {
			Utils.createFile(this.getRootPath(), "{}");
		}
	}

	/**
	 * Save or update property to storage
	 * 
	 * @param prop
	 * @param value
	 * @throws IOException
	 */
	public void saveProp(String prop, String value) throws IOException {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(prop, value);
		Utils.createFile(this.getRootPath(), jsonObject.toString());
	}

	/**
	 * Save or update property to storage
	 * 
	 * @param prop
	 * @param value
	 * @throws IOException
	 */
	public void removeProp(String prop) throws IOException {
		JsonObject jsonObject = new JsonObject();
		jsonObject.remove(prop);
	}

	/**
	 * Get property from storage
	 * 
	 * @param prop
	 * @return String
	 * @throws IOException
	 */
	public String getString(String prop) throws IOException {
		String contents = Utils.readFile(this.getRootPath());

		JsonObject parsed = (JsonObject) new JsonParser().parse(contents);

		JsonElement element = parsed.get(prop);

		return element != null ? element.getAsString() : null;
	}

	/**
	 * Get the path where the plugin storage is setted
	 * 
	 * @return String
	 */
	private String getRootPath() {
		Bundle bundle = FrameworkUtil.getBundle(getClass());

		IPath stateLoc = Platform.getStateLocation(bundle);
		return String.format("%s/storage.json", stateLoc.toString());
	}

}
