package codetrack;

import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Storage {
	public void saveProp(String prop, String value) throws IOException {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(prop, value);
		Utils.createFile(this.getRootPath(), jsonObject.toString());

	}

	public String getString(String prop) throws IOException {
		String contents = Utils.readFile(this.getRootPath());

		JsonObject parsed = (JsonObject) new JsonParser().parse(contents);

		return parsed.get(prop).getAsString();
	}

	private String getRootPath() {
		Bundle bundle = FrameworkUtil.getBundle(getClass());

		IPath stateLoc = Platform.getStateLocation(bundle);
		return String.format("%s/storage.json", stateLoc.toString());
	}
}
