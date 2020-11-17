package codetrack.views;

import org.eclipse.swt.widgets.Composite;

import java.util.ArrayList;

import javax.swing.SwingWorker;

import org.eclipse.swt.graphics.Color;
import swing2swt.layout.BorderLayout;

public class CompositeWithLoader extends Composite {

	private StatusBar statusBar;

	public CompositeWithLoader(Composite parent, int style) {
		super(parent, style);
		setLayout(new BorderLayout(0, 0));
		statusBar = new StatusBar(this, style);
		statusBar.setLayoutData(BorderLayout.NORTH);
	}

	public void setLoading(String text, boolean loading) {
		statusBar.getDisplay().asyncExec(new Runnable() {
		    @Override
		    public void run() {
		        if (statusBar.isDisposed()) return;
		        statusBar.setLoading(loading);
		        statusBar.setText(text);
		        
		        setEnabled(!loading);
		        
		    }
		});
	}


	public void setError(String errorMsg) {
		Color RED = new Color(255, 0, 0);
		statusBar.setForeground(RED);
		statusBar.setText(errorMsg);
	}

}
