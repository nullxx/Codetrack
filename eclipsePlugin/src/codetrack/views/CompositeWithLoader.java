package codetrack.views;

import org.eclipse.swt.widgets.Composite;
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
		this.setEnabled(!loading);
		statusBar.setText(text);
		statusBar.autoProgressBar();
	}

	public void setLoading(String text, boolean loading, int progress) {
		statusBar.setText(text);
		statusBar.setProgress(progress);
	}

	public void setError(String errorMsg) {
		Color RED = new Color(255, 0, 0);
		statusBar.setForeground(RED);
		statusBar.setText(errorMsg);
	}

}
