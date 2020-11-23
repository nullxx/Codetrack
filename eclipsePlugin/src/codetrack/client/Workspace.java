package codetrack.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

public class Workspace {

	/**
	 * Get the localProjects in the workspace
	 * @return LocalProject[]
	 * @throws IOException
	 */
	public static LocalProject[] getLocalProjects() throws IOException {
		LocalProject[] projectList = null;
		int j = 0;
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] projects = workspaceRoot.getProjects();
		projectList = new LocalProject[projects.length];
		for (int i = 0; i < projects.length; i++) {
			IProject project = projects[i];
			if (project.isOpen()) {
				LocalProject p = new LocalProject(project);
				projectList[j] = p;
				j++;
			}
		}

		return projectList;
	}

	/**
	 * Captures a snapshot of the current opened file
	 * @return Snapshot
	 * @throws IOException
	 */
	public static Snapshot captureSnapshot() throws IOException {
		IWorkbench wb = PlatformUI.getWorkbench();
		if (wb.getWorkbenchWindowCount() == 1) {
				IWorkbenchPart workbenchPart = wb.getWorkbenchWindows()[0].getActivePage().getActivePart();
				IFile file = (IFile) workbenchPart.getSite().getPage().getActiveEditor().getEditorInput()
						.getAdapter(IFile.class);
				if (file == null)
					throw new FileNotFoundException();
				File f = new File(file.getLocation().toString());
				
				Project p = Project.fromFile(f);
				if (p != null) { // if not processed projects yet
					Snapshot snapshot = new Snapshot(p, f);
					return snapshot;
				}
		}
		return null;
	}
}