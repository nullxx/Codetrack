package codetrack.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
	 * 
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

	/**
	 * Process saved projects see
	 * {@link #processProjects(LocalProject[], RemoteProject[], boolean)}
	 * 
	 * @return Project[]
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws CoreException
	 */
	public static Project[] processProjects() throws IOException, InterruptedException, CoreException {
		LocalProject[] localProjects = Workspace.getLocalProjects();
		RemoteProject[] cloudProjects = (RemoteProject[]) RestAPI.getAllowedProjects(false);
		return Project.processProjects(localProjects, cloudProjects, true);
	}

	/**
	 * Processes localProjects and remoteProjects
	 * 
	 * @param localProjects
	 * @param cloudProjects
	 * @param save
	 * @return Project[]
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws CoreException
	 */
	public static Project[] processProjects(LocalProject[] localProjects, RemoteProject[] cloudProjects, boolean save)
			throws IOException, InterruptedException, CoreException {
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
	 * 
	 * @param localProject
	 * @return Project
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws CoreException
	 */
	private static Project createRemoteProject(LocalProject localProject)
			throws IOException, InterruptedException, CoreException {
		// create remoteProject and link it with local one
		HashMap<String, Object> initialProjectData = new HashMap<String, Object>();
		initialProjectData.put("name", localProject.getOriginalProject().getName());
		String[] naturesIds = localProject.getOriginalProject().getDescription().getNatureIds();

		initialProjectData.put("natures", String.join(",", naturesIds));
		// send to server natures, the server will interpret them and set the language
		// of the project
		RemoteProject created = RestAPI.createProject(initialProjectData);

		localProject.setProjectConfig(new LocalProjectConfig(created.getId(), created.getName(), true));

		localProject.reSaveProjectConfig();
		Project project = new Project(localProject, created);
		return project;
	}

	/**
	 * Do a full sync
	 * 
	 * @return success
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws CoreException
	 */
	public boolean syncFullProject() throws IOException, InterruptedException, CoreException {
		if (this.getLocalProject().getProjectConfig().isAllowed()) {
			ArrayList<File> files = this.getLocalProject().getFullData();
			for (int i = 0; i < files.size(); i++) {
				HashMap<String, Object> data = new HashMap<String, Object>();
				File file = files.get(i);
				String projectPath = this.getLocalProject().getOriginalProject().getLocation().toString();
				String fileParentFolderPath = file.getParent().toString();

				String relativePath = fileParentFolderPath.substring(
						fileParentFolderPath.lastIndexOf(projectPath) + projectPath.length(),
						fileParentFolderPath.length());

				data.put("project", this.getRemoteProject().getId());
				data.put("localPath", relativePath);
				data.put("data", file.toPath());
				boolean success = RestAPI.createSnapshot(data);
				if (!success) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @param action if is not sync, sync now
	 * @return boolean true if project is already sync or has synqued since action
	 *         param = true
	 * @throws CoreException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public boolean isFullFilledFistSync(boolean action, boolean force)
			throws IOException, InterruptedException, CoreException {
		LocalProject localProject = this.getLocalProject();
		if (!localProject.getProjectConfig().isFullFirstSyncDone()) {
			if (!action) {
				return false;
			}
			return this.triggerSync(localProject);
		} else if (force) {
			return this.triggerSync(localProject);
		}
		return true;
	}

	/**
	 * Triggers sync for a LocalProject
	 * 
	 * @param localProject
	 * @return success
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws CoreException
	 */
	private boolean triggerSync(LocalProject localProject) throws IOException, InterruptedException, CoreException {
		boolean syncDone = this.syncFullProject();
		localProject.getProjectConfig().setFullFirstSyncDone(syncDone);
		localProject.reSaveProjectConfig();
		return syncDone;
	}

	private static void saveProjects(Project[] projects) {
		Project.projects = projects;
	}

	/**
	 * Get the opened Project in the Workspace
	 * 
	 * @return Project
	 * @throws FileNotFoundException
	 */
	public static Project getOpened() throws FileNotFoundException {
		return Project.fromFile(Workspace.getOpenedFile());
	}

	/**
	 * Get a project (from saved ones) from its id
	 * 
	 * @param projectId
	 * @return Project
	 */
	public static Project fromId(int projectId) {
		Project p = null;
		for (int i = 0; i < Project.projects.length; i++) {
			if (Project.projects[i].getRemoteProject().getId() == projectId) {
				p = Project.projects[i];
			}
		}
		return p;
	}

	public String toString() {
		if (this.getLocalProject().getProjectConfig().isAllowed()) {
			return String.format("✅ SYNC ENABLED - %s", this.getLocalProject().toString());
		}
		return String.format("❎️ SYNC DISABLED - %s", this.getLocalProject().toString());
	}

	/**
	 * Format in JSON
	 * 
	 * @return JsonObject
	 */
	public JsonObject formatJSON() {
		JsonObject json = new JsonObject();
		RemoteProject rm = this.getRemoteProject();
		json.addProperty("id", rm.getId());
		json.addProperty("name", rm.getName());
		json.addProperty("user", rm.getUser());
		json.addProperty("natures", String.join(",", rm.getProjectNatures()));
		json.addProperty("createdAt", rm.getCreatedAt());
		json.addProperty("updatedAt", rm.getUpdatedAt());
		json.addProperty("isAllowed", rm.isAllowed());
		return json;
	}

	/**
	 * Format in JSON
	 * 
	 * @return JsonArray
	 */
	public static JsonArray projectsJSON(Project[] projects) {
		JsonArray jsonArray = new JsonArray();
		for (int i = 0; i < projects.length; i++) {
			jsonArray.add(projects[i].formatJSON());
		}
		return jsonArray;
	}
}
