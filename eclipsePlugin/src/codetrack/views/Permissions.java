package codetrack.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import codetrack.client.Project;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.SwingWorker;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import codetrack.api.RestAPI;

public class Permissions extends TabViewListener {

	private CheckboxTableViewer checkboxTableViewer;

	public Permissions(TabFolder parent, int style, String title) {
		super(parent, style, title);

		Composite composite_5 = new Composite(parent, style);
		composite_5.setLayout(new FillLayout(SWT.HORIZONTAL));
		Composite composite_6 = new Composite(composite_5, style);
		composite_6.setLayout(new FillLayout(SWT.HORIZONTAL));

		checkboxTableViewer = CheckboxTableViewer.newCheckList(composite_6, SWT.FULL_SELECTION | SWT.BORDER);

		checkboxTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		checkboxTableViewer.setLabelProvider(new LabelProvider());

		this.addListener(new TabFocusListener() {
			@Override
			public void refresh() {
				Permissions.this.setProjects();

			}
		});
		checkboxTableViewer.addCheckStateListener(new ICheckStateListener() {

			@Override
			public void checkStateChanged(CheckStateChangedEvent e) {
				Project project = (Project) e.getElement(); // source data must always be a Project !! if not cast will
															// fail
				Permissions.this.changePermisions(e.getChecked(), project.getRemoteProject().getId());
			}

		});
		this.setControl(composite_5);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private void setProjects() {
		Project[] projects = Project.projects;
		checkboxTableViewer.setInput(projects);

		this.processChecks(projects);
	}

	private void reloadProjects() throws IOException, InterruptedException {
		Project.processProjects();
	}

	private void changePermisions(boolean isAllowed, int project) {
		CompositeWithLoader parent = (CompositeWithLoader) this.getParent().getParent();
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				SwingWorker<?, ?> call = new SwingWorker<Object, Object>() {
					@Override
					protected Object doInBackground() throws Exception {
						parent.setLoading("Updating...", true);
						HashMap<String, Object> updateData = new HashMap<String, Object>();
						updateData.put("project", project);
						updateData.put("isAllowed", isAllowed);

						try {
							boolean successUpdating = RestAPI.updateProject(updateData);
							if (successUpdating) {
								reloadProjects();
							} else {
								parent.setError("Something wrong updating");
							}

						} catch (IOException | InterruptedException e) {
							parent.setError(String.format("Error updating: %s", e.getMessage()));
						}
						this.done();
						return null;
					}

				};

				call.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
							parent.setLoading("Updated!", false);
						}

					}

				});
				call.execute();
			};

		});
	}

	private void processChecks(Project[] projects) {
		for (int i = 0; i < projects.length; i++) {
			Project project = projects[i];
			checkboxTableViewer.setChecked(project, project.getLocalProject().getProjectConfig().isAllowed());
		}
	}

}
