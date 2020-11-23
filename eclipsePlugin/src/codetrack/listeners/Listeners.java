/**
 * 
 */
package codetrack.listeners;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;

import codetrack.api.RestAPI;
import codetrack.client.Project;
import codetrack.client.Snapshot;
import codetrack.client.Workspace;

/**
 * @author nullx
 *
 */
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

						Project project = snapshot.getProject();

						project.isFullFilledFistSync(true);

						if (snapshot != null) { // if save is toggled before processing projects
							HashMap<String, Object> data = new HashMap<String, Object>();
							data.put("project", project.getRemoteProject().getId());
							data.put("localPath", snapshot.getFile().getParent());
							data.put("data", snapshot.getFile().toPath());
							if (project.getLocalProject().getProjectConfig().isAllowed()) {
								RestAPI.createSnapshot(data);
							}
						}

					} catch (IOException | InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		};
		ResourcesPlugin.getWorkspace().addResourceChangeListener(listener);
	}

}
