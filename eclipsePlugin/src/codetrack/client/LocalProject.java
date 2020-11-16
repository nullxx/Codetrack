package codetrack.client;

import java.io.IOException;

import org.eclipse.core.resources.IProject;

import com.google.gson.Gson;

import codetrack.Config;
import codetrack.Utils;

public class LocalProject {
	private static final String projectConfigPath = Config.LOCAL_PROJECT_FILE_INFO;

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
		if (LocalProjectConfig.exists(localPath)) {
			this.loadConfig(localPath);
		}
	}

	public LocalProjectConfig getProjectConfig() {
		return projectConfig;
	}

	public void setProjectConfig(LocalProjectConfig projectConfig) throws IOException {
		this.projectConfig = projectConfig;
	}
	
	public void reSaveProjectConfig() throws IOException {
		String localPath = this.getConfigFilePath();
		this.save(localPath);
	}

	private void save(String localPath) throws IOException {
		Gson gson = new Gson();
		Utils.createFile(localPath, gson.toJson(this.getProjectConfig()));
	}

	private void loadConfig(String localPath) throws IOException {
		Gson gson = new Gson();
		String fileContents = Utils.readFile(localPath);
		LocalProjectConfig localProjectConfig = gson.fromJson(fileContents, LocalProjectConfig.class);
		this.setProjectConfig(localProjectConfig);
	}

	private String getConfigFilePath() {
		String localPath = String.format("%s/%s", this.getProjectRootPath(), projectConfigPath);
		return localPath;
	}
	private String getProjectRootPath() {
		return this.originalProject.getLocation().toString();
	}

	public boolean isInited() {
		return this.getProjectConfig() != null;
	}

	public String toString() {
		return String.format("%s (%s)", this.getOriginalProject().getName(), this.getProjectRootPath());
	}
}
