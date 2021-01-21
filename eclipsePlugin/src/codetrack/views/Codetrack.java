package codetrack.views;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.part.ViewPart;

public class Codetrack extends ViewPart {

	@Inject
	IWorkbench workbench;

	private MainView mView;

	public Codetrack() {
		super();
	}

	public void setFocus() {
		mView.setFocus();
	}

	public void createPartControl(Composite parentView) {
		mView = new MainView(parentView, SWT.NONE);
		System.out.println("---- PLUGIN LOADED ----");
	}
}
