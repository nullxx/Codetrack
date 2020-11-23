package codetrack.client;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.core.resources.IProject;

import codetrack.api.RestAPI;

public class Project {

	private LocalProject localProject;
	private RemoteProject remoteProject;
	public static Project[] projects = {};

	public Project(LocalProject localProject, RemoteProject remoteProject) {
		this.setLocalProject(localProject);
		this.setRemoteProject(remoteProject);
	}

	public LocalProject getLocalProject() {
		return localProject;
	}

	public void setLocalProject(LocalProject localProject) {
		this.localProject = localProject;
	}

	public RemoteProject getRemoteProject() {
		return remoteProject;
	}

	public void setRemoteProject(RemoteProject remoteProject) {
		this.remoteProject = remoteProject;
	}
	/**
	 * Find a project given a file of it
	 * @param file
	 * @return Project
	 */
	public static Project fromFile(File file) {
		for (int i = 0; i < Project.projects.length; i++) {
			IProject iproject = Project.projects[i].getLocalProject().getOriginalProject();
			if (iproject.getFile(file.getPath()).getFullPath().toString()
					.indexOf(iproject.getLocation().toString()) != -1) {
				return Project.projects[i];
			}
		}
		return null;
	}
	
	public static Project[] processProjects() throws IOException, InterruptedException {
		LocalProject[] localProjects = Workspace.getLocalProjects();
		RemoteProject[] cloudProjects = RestAPI.getAllowedProjects();
		return Project.processProjects(localProjects, cloudProjects, true);
	}
	/**
	 * Processes localProjects and remoteProjects
	 * @param localProjects
	 * @param cloudProjects
	 * @param save
	 * @return Project[]
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static Project[] processProjects(LocalProject[] localProjects, RemoteProject[] cloudProjects, boolean save)
			throws IOException, InterruptedException {
		Project[] projects = new Project[localProjects.length];
		int k = 0;
		for (int i = 0; i < localProjects.length; i++) {
			LocalProject localProject = localProjects[i];
			Project temp = null;

			if (localProject.isInited()) {
				LocalProjectConfig localProjectConfig = localProject.getProjectConfig();
				// check if the config matches with the server
				boolean found = false;

				remoteLoop: for (int j = 0; j < cloudProjects.length; j++) {
					RemoteProject remoteProject = cloudProjects[j];

					if (remoteProject.getId().equals(localProjectConfig.getId())) {
						// localProject matches with the server one => do nothing
						localProjectConfig.setAllowed(remoteProject.isAllowed()); // this means it was allowed because
																					// is in the server
						localProject.reSaveProjectConfig(); // to save setAllowed(true) to file
						temp = new Project(localProject, remoteProject);
						found = true;
						break remoteLoop;
					}
				}
				if (found == false) {
					// localProject has config that the server does not have (very weird) but ==>
					// create new project and link it to localproject
					if (localProjectConfig.isAllowed()) {
						temp = Project.createRemoteProject(localProject);
					}
				}
			} else {
				// create a remote project and link it with local
				temp = Project.createRemoteProject(localProject); // BY DEFAULT WE CREATE A PROJECT AND ALLOW IT
			}
			projects[k] = temp;
			k++;
		}
		if (save) {
			Project.saveProjects(projects);
		}
		return projects;
	}
	/**
	 * Create remoteProject given a localProject
	 * @param localProject
	 * @return Project
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static Project createRemoteProject(LocalProject localProject) throws IOException, InterruptedException {
		// create remoteProject and link it with local one
		HashMap<String, Object> initialProjectData = new HashMap<String, Object>();
		initialProjectData.put("name", localProject.getOriginalProject().getName());
//		initialProjectData.put("language", "test");
		// send to server natures, the server will interpret them and set the language
		// of the project
		RemoteProject created = RestAPI.createProject(initialProjectData);

		localProject.setProjectConfig(new LocalProjectConfig(created.getId(), created.getName(), true));

		localProject.reSaveProjectConfig();
		Project project = new Project(localProject, created);
		return project;
	}

	public boolean syncFullProject() {
		// TODO
		return false;
	}

	/**
	 * @param action  if is not sync, sync now
	 * @return boolean true if project is already sync or has synqued since action param = true
	 */
	public boolean isFullFilledFistSync(boolean action) {
		LocalProject localProject = this.getLocalProject();
		if (!localProject.getProjectConfig().isFullFirstSyncDone()) {
			if (!action)
				return false;
			boolean syncDone = this.syncFullProject();
			localProject.getProjectConfig().setFullFirstSyncDone(syncDone);
		}
		return true;
	}

	private static void saveProjects(Project[] projects) {
		Project.projects = projects;
	}

	public String toString() {
		if (this.getLocalProject().getProjectConfig().isAllowed()) {
			return String.format("✅ SYNC ENABLED - %s", this.getLocalProject().toString());
		}
		return String.format("❎️ SYNC DISABLED - %s", this.getLocalProject().toString());
	}

}
