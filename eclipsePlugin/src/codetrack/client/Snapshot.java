package codetrack.client;

import java.io.File;

public class Snapshot {
	private Project project;
	private File file;

	public Snapshot(Project project, File file) {
		this.project = project;
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
