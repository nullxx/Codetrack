package codetrack.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import codetrack.Utils;

public class Workspace {

	/**
	 * Get the localProjects in the workspace
	 * 
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
	 * Returns the opened file in the workbench
	 * 
	 * @return File
	 * @throws FileNotFoundException
	 */
	public static File getOpenedFile() throws FileNotFoundException {
		IWorkbench wb = PlatformUI.getWorkbench();
		if (wb.getWorkbenchWindowCount() == 1) {
			IWorkbenchPart workbenchPart = wb.getWorkbenchWindows()[0].getActivePage().getActivePart();
			IFile file = (IFile) workbenchPart.getSite().getPage().getActiveEditor().getEditorInput()
					.getAdapter(IFile.class);
			if (file == null)
				throw new FileNotFoundException();
			File f = new File(file.getLocation().toString());

			return f;
		}
		return null;
	}

	/**
	 * Captures a snapshot of the current opened file
	 * 
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

	/**
	 * Create a localProject in the current workspace
	 * 
	 * @param p
	 * @param compressedFilePath
	 * @throws CoreException
	 * @throws IOException
	 */
	public static void createProject(RemoteProject p, String compressedFilePath) throws CoreException, IOException {

		IProgressMonitor progressMonitor = new NullProgressMonitor();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(String.format("dw - %s", p.getName()));
		project.create(progressMonitor);
		project.open(null);

		IProjectDescription pDescription = project.getDescription();
		pDescription.setNatureIds(p.getProjectNatures());
		project.setDescription(pDescription, null);

		Utils.unzip(compressedFilePath, project.getLocation().toString());

	}
}