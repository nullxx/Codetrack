package codetrack;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Utils {

	private final static int BUFFER_SIZE = 2048;

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
	 * 
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
	 * 
	 * @param jsonStr
	 * @return JsonObject
	 */
	public static JsonObject parseJSON(String jsonStr) {
		JsonObject parsed = (JsonObject) new JsonParser().parse(jsonStr);
		return parsed;

	}

	/**
	 * Creates a custom exception. Displays an alert.
	 * 
	 * @param e
	 * @return error message
	 */
	public static String handleException(Exception e) {
		e.printStackTrace();
		String message = String.format("Error: %s", e.getMessage());
		Utils.createAlert(Display.getDefault(), "Error", message);
		return message;
	}

	/**
	 * Unzip a zip into a directory
	 * 
	 * @param zipFilePath
	 * @param destDirectory
	 * @throws IOException
	 */
	public static void unzip(String zipFilePath, String destDirectory) throws IOException {

		try {
			// Open the zip file
			ZipFile zipFile = new ZipFile(zipFilePath);
			Enumeration<?> enu = zipFile.entries();
			while (enu.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enu.nextElement();

				String name = zipEntry.getName();
				long size = zipEntry.getSize();
				long compressedSize = zipEntry.getCompressedSize();
				System.out.printf("name: %-20s | size: %6d | compressed size: %6d\n", name, size, compressedSize);

				// Do we need to create a directory ?
				File file = new File(destDirectory + File.separator + name);
				if (name.endsWith("/")) {
					file.mkdirs();
					continue;
				}

				File parent = file.getParentFile();
				if (parent != null) {
					parent.mkdirs();
				}

				// Extract the file
				InputStream is = zipFile.getInputStream(zipEntry);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] bytes = new byte[BUFFER_SIZE];
				int length;
				while ((length = is.read(bytes)) >= 0) {
					fos.write(bytes, 0, length);
				}
				is.close();
				fos.close();

			}
			zipFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * List files from path excluding {".git", ".class"}
	 * 
	 * @param dir
	 * @param recursive
	 * @return ArrayList<File>
	 */
	public static ArrayList<File> listFileTree(File dir, boolean recursive) {
		if (null == dir || !dir.isDirectory()) {
			return new ArrayList<>();
		}
		final Set<File> fileTree = new HashSet<File>();
		FileFilter fileFilter = new FileFilter() {

			@Override
			public boolean accept(File file) {
				if (file.isDirectory() && file.getName().endsWith(".git")) {
					return false;
				} else if (file.getName().endsWith(".class")) {
					return false;
				}
				return true;
			}
		};
		File[] listed = dir.listFiles(fileFilter);

		if (listed != null) {
			for (File entry : listed) {
				if (entry.isFile()) {
					fileTree.add(entry);
				} else if (recursive) {
					fileTree.addAll(listFileTree(entry, true));
				}
			}
		}
		return new ArrayList<>(fileTree);
	}

	/**
	 * Creates a screen dialog
	 * 
	 * @param display
	 * @param title
	 * @param message
	 */
	public static void createAlert(Display display, String title, String message) {
		MessageBox box = new MessageBox(display.getActiveShell(), SWT.OK);
		box.setText(title);
		box.setMessage(message);
		box.open();
	}

	/**
	 * Force shell to focus
	 * 
	 * @param shell
	 */
	public static void forceActive(final Shell shell) {
		shell.getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (shell != null && !shell.isDisposed()) {
					shell.forceActive();
				}
			}
		});
	}
}
