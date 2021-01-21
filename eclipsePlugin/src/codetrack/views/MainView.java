package codetrack.views;

import org.eclipse.swt.widgets.Composite;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import codetrack.Config;
import codetrack.Utils;
import codetrack.api.HttpCaller;
import codetrack.api.RestAPI;
import codetrack.client.Group;
import codetrack.client.Observation;
import codetrack.client.Project;
import codetrack.client.RemoteProject;
import codetrack.client.User;
import codetrack.client.Workspace;
import codetrack.listeners.Listeners;

public class MainView extends CompositeWithLoader {
	RichClient loginView;
	RichClient projectListView;
	RichClient adminView;
	private final String BASE_URL = Config.CLIENT_BASE_URL;

	public MainView(Composite parent, int style) {
		super(parent, style);

		this.initListeners();

		this.launchLoginView();
	}

	// start login

	/**
	 * Launch login view
	 */
	private void launchLoginView() {
		loginView = new RichClient(this, String.format("%s/%s", BASE_URL, "login.html"));
		loginView.addListener(new BrowserListener() {
			@Override
			public Object bCallback(BrowserListenerResponse browserListenerResponse) {
				if (browserListenerResponse.getType() == BrowserListenerResponseType.LOGIN) {
					JsonObject data = browserListenerResponse.getData();
					String email = data.get("email").getAsString();
					String password = data.get("password").getAsString();
					return MainView.this.login(email, password);
				}
				return false;
			}
		});
		layout(true, true);
	}

	/**
	 * Manages login request
	 * 
	 * @param email
	 * @param password
	 * @return success
	 */
	private boolean login(String email, String password) {
		try {
			User logged = RestAPI.login(email, password);
			if (logged != null) {
				loginView.dispose();
				if (logged.isAdmin()) {
					launchAdminView();
				} else {
					launchProjectListView();
				}

				return true;
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}
	// end login

	// start projectList

	/**
	 * Launches project list view
	 */
	private void launchProjectListView() {
		projectListView = new RichClient(this, String.format("%s/%s", BASE_URL, "userCheckProjects.html"));
		projectListView.addListener(new BrowserListener() {
			@Override
			public Object bCallback(BrowserListenerResponse browserListenerResponse) {
				Object toReturn = null;

				JsonObject data = browserListenerResponse.getData();
				switch (browserListenerResponse.getType()) {
				case GET_PROJECT_LIST:
					toReturn = getProjectListStr();
					break;
				case SET_PROJECT_STATUS:
					updateProject(data);
					toReturn = getProjectListStr();
					break;
				case SYNC_FULL_PROJECT:
					toReturn = syncFullProject(data);
					break;
				case LOG_OUT:
					logOut(projectListView);
					break;
				default:
					break;
				}
				return toReturn;
			}
		});
		layout(true, true);
	}

	/**
	 * Get the local project list
	 * 
	 * @return json string formatted
	 */
	private String getProjectListStr() {
		try {
			JsonArray projectList = (JsonArray) Project.projectsJSON(Project.processProjects());
			return projectList.toString();
		} catch (IOException | InterruptedException | CoreException e) {
			Utils.handleException(e);
		}
		return null;
	}

	/**
	 * Manages the update of a project
	 * 
	 * @param data to update
	 * @return success
	 */
	private boolean updateProject(JsonObject data) {
		try {
			HashMap<String, Object> updateData = new HashMap<String, Object>();
			updateData.put("project", data.get("project").getAsInt());
			updateData.put("isAllowed", data.get("isAllowed").getAsBoolean());

			return RestAPI.updateProject(updateData);
		} catch (IOException | InterruptedException e) {
			Utils.handleException(e);
		}
		return false;
	}

	/**
	 * Triggers sync for a project
	 * 
	 * @param data
	 * @return
	 */
	private boolean syncFullProject(JsonObject data) {
		int projectId = data.get("project").getAsInt();
		Project p = Project.fromId(projectId);
		try {
			return p.isFullFilledFistSync(true, true);
		} catch (IOException | InterruptedException | CoreException e) {
			Utils.handleException(e);
		}
		return false;
	}
	// end projectList

	// start adminView
	/**
	 * Launches admin view
	 */
	private void launchAdminView() {
		adminView = new RichClient(this, String.format("%s/%s", BASE_URL, "adminView.html"));
		adminView.addListener(new BrowserListener() {
			@Override
			public Object bCallback(BrowserListenerResponse browserListenerResponse) {
				Object toReturn = null;

				JsonObject data = browserListenerResponse.getData();
				switch (browserListenerResponse.getType()) {
				case ADMIN_GET_GROUP_LIST:
					toReturn = getGroupListStr();
					break;
				case ADMIN_GET_GROUP_OBSERVATIONS:
					int groupId = data.get("group").getAsInt();
					toReturn = getObservationListStr(groupId);
					break;
				case ADMIN_GET_OBSERVATION_PROJECTS:
					String observationId = data.get("shortId").getAsString();
					toReturn = getObservationProjectListStr(observationId);
					break;
				case ADMIN_DOWNLOAD_PROJECT:
					int projectId = data.get("project").getAsInt();
					downloadProject(projectId);
					break;
				case LOG_OUT:
					logOut(adminView);
					break;
				default:
					break;
				}
				return toReturn;
			}
		});
		layout(true, true);
	}

	/**
	 * Get the group list
	 * 
	 * @return json string formatted
	 */
	private String getGroupListStr() {
		try {
			JsonArray groupList = (JsonArray) Group.groupsJSON(RestAPI.getGroups());
			return groupList.toString();
		} catch (IOException | InterruptedException e) {
			Utils.handleException(e);
		}
		return null;
	}

	/**
	 * Get the observation list
	 * 
	 * @return json string formatted
	 */
	private String getObservationListStr(int groupId) {
		try {
			Group group = new Group(groupId);
			Observation[] observations = group.getObservations();
			JsonArray observationList = (JsonArray) Observation.observationsJSON(observations);
			return observationList.toString();
		} catch (IOException | InterruptedException e) {
			Utils.handleException(e);
		}
		return null;
	}

	/**
	 * Get the observation project list
	 * 
	 * @return json string formatted
	 */
	private String getObservationProjectListStr(String shortId) {

		try {
			RemoteProject[] projects = RestAPI.getObservationProjects(shortId);
			JsonArray projectList = (JsonArray) RemoteProject.remoteProjectsJSON(projects);
			return projectList.toString();
		} catch (IOException | InterruptedException e) {
			Utils.handleException(e);
		}
		return null;
	}

	/**
	 * Triggers the download of a project
	 * 
	 * @param projectId
	 */
	private void downloadProject(int projectId) {
		try {
			RemoteProject p = RestAPI.getProject(projectId);
			Path ds = RestAPI.downloadProject(projectId);
			Workspace.createProject(p, ds.toAbsolutePath().toString());
		} catch (IOException | InterruptedException | CoreException e) {
			Utils.handleException(e);
		}

	}
	// end adminView

	/**
	 * Logs out from a RichClient view
	 * 
	 * @param from
	 */
	private void logOut(RichClient from) {
		try {
			from.dispose();
			HttpCaller.logOut();
			launchLoginView();
		} catch (IOException e) {
			Utils.handleException(e);
		}
	}

	/**
	 * Start listeners
	 */
	private void initListeners() {
		try {
			new Listeners();
		} catch (Exception e) {
			Utils.handleException(e);
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
