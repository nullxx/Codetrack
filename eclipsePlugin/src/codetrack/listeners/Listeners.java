/**
 * 
 */
package codetrack.listeners;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import codetrack.api.RestAPI;
import codetrack.client.Project;
import codetrack.client.Snapshot;
import codetrack.client.Workspace;

public class Listeners {

	ResourcesPlugin resPlugin;

	public Listeners() throws Exception {
		this.init();
	}

	private void init() {
		this.onSave();
	}

	/**
	 * Will be triggered when saved file
	 */
	private void onSave() {
		IResourceChangeListener listener = new IResourceChangeListener() {
			@Override
			public void resourceChanged(IResourceChangeEvent e) {
				if (e.getType() == IResourceChangeEvent.POST_CHANGE) {
					// postchange will be triggered more than one time after save
					System.out.println("POST_CHANGE");
					try {
						Snapshot snapshot = Workspace.captureSnapshot();
						if (snapshot == null)
							return;
						Project project = snapshot.getProject();
						project.isFullFilledFistSync(true, false);

						if (snapshot != null) { // if save is toggled before processing projects
							HashMap<String, Object> data = new HashMap<String, Object>();
							data.put("project", project.getRemoteProject().getId());
							String projectPath = project.getLocalProject().getOriginalProject().getLocation()
									.toString();
							String fileParentFolderPath = snapshot.getFile().getParent().toString();

							String relativePath = fileParentFolderPath.substring(
									fileParentFolderPath.lastIndexOf(projectPath) + projectPath.length(),
									fileParentFolderPath.length());

							data.put("localPath", relativePath);
							data.put("data", snapshot.getFile().toPath());
							if (project.getLocalProject().getProjectConfig().isAllowed()) {
								RestAPI.createSnapshot(data);
							}
						}

					} catch (IOException | InterruptedException | CoreException e1) {
						e1.printStackTrace();
					}
				}
			}
		};
		ResourcesPlugin.getWorkspace().addResourceChangeListener(listener);
	}

}
