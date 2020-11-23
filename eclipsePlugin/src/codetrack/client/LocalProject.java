package codetrack.client;

import java.io.IOException;

import org.eclipse.core.resources.IProject;

import com.google.gson.Gson;

import codetrack.Config;
import codetrack.Utils;

public class LocalProject {
	/**
	 * Native local project
	 */
	private IProject originalProject;
	private LocalProjectConfig projectConfig;

	public LocalProject(IProject originalProject) throws IOException {
		this.setOriginalProject(originalProject);
	}
	
	public IProject getOriginalProject() {
		return originalProject;
	}

	public void setOriginalProject(IProject originalProject) throws IOException {
		this.originalProject = originalProject;

		String localPath = this.getConfigFilePath();
		if (LocalProjectConfig.exists(localPath)) { // if localProject has config, load it
			this.loadConfig(localPath);
		}
	}

	public LocalProjectConfig getProjectConfig() {
		return projectConfig;
	}

	public void setProjectConfig(LocalProjectConfig projectConfig) throws IOException {
		this.projectConfig = projectConfig;
	}
	
	/**
	 * Save the config to the localProject storage
	 * @throws IOException
	 */
	public void reSaveProjectConfig() throws IOException {
		String localPath = this.getConfigFilePath();
		this.save(localPath);
	}

	/**
	 * Saves the current localProjectConfig to a given path 
	 * @param localPath
	 * @throws IOException
	 */
	private void save(String localPath) throws IOException {
		Gson gson = new Gson();
		Utils.createFile(localPath, gson.toJson(this.getProjectConfig()));
	}
	
	/**
	 * Loads the localProjectConfig from the stored file
	 * @param localPath
	 * @throws IOException
	 */
	private void loadConfig(String localPath) throws IOException {
		Gson gson = new Gson();
		String fileContents = Utils.readFile(localPath);
		LocalProjectConfig localProjectConfig = gson.fromJson(fileContents, LocalProjectConfig.class);
		this.setProjectConfig(localProjectConfig);
	}
	/**
	 * Returns the localProject file config path
	 * @return String
	 */
	private String getConfigFilePath() {
		String localPath = String.format("%s/%s", this.getProjectRootPath(), Config.LOCAL_PROJECT_FILE_INFO);
		return localPath;
	}
	/**
	 * Returns the localProject root path
	 * @return String
	 */
	private String getProjectRootPath() {
		return this.originalProject.getLocation().toString();
	}
	/**
	 * if true => project has not config stored
	 * @return boolean
	 */
	public boolean isInited() {
		return this.getProjectConfig() != null;
	}

	public String toString() {
		return String.format("%s (%s)", this.getOriginalProject().getName(), this.getProjectRootPath());
	}
}
