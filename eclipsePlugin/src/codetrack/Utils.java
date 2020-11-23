package codetrack;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Utils {
	/**
	 * Create or overwrite and existing file
	 * 
	 * @param path
	 * @param content
	 * @throws IOException
	 */
	public static void createFile(String path, String content) throws IOException {
		FileWriter writer = new FileWriter(path, false); // not appending
		writer.write(content);
		writer.flush();
		writer.close();
	}

	/**
	 * Read a file from a given path
	 * @param path
	 * @return String
	 * @throws IOException
	 */
	public static String readFile(String path) throws IOException {
		FileReader reader = new FileReader(path);
		String contents = "";
		int c;
		while ((c = reader.read()) != -1) {
			contents += (char) c;
		}
		reader.close();
		return contents;
	}

	public static boolean fileExists(String filePath) {
		File f = new File(filePath);
		return f.exists();
	}

	/**
	 * Parse JSON given a JSON String
	 * @param jsonStr
	 * @return JsonObject
	 */
	public static JsonObject parseJSON(String jsonStr) {
		JsonObject parsed = (JsonObject) new JsonParser().parse(jsonStr);
		return parsed;

	}

}
