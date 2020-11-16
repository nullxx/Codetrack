package codetrack.views;

import javax.inject.Inject;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.part.ViewPart;


public class Codetrack extends ViewPart {

	@Inject
	IWorkbench workbench;

	private Composite mView;

	public Codetrack() {
		super();
	}

	public void setFocus() {
		mView.setFocus();
	}

	public void createPartControl(Composite parentView) {
		mView = new MainView(parentView, 0);		 
		System.out.println("---- PLUGIN LOADED ----");
	}
}
