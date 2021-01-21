package codetrack.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

import codetrack.Config;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;

import swing2swt.layout.BorderLayout;

public class CompositeWithLoader extends Composite {

	Label lblNewLabel;
	ProgressBar progressBar;
	Composite composite;

	public CompositeWithLoader(Composite parent, int style) {
		super(parent, style);
		setLayout(new BorderLayout(0, 0));

		composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(BorderLayout.NORTH);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));

		lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setText(Config.TABBAR_DEFAULT_TITLE);

		Composite composite_1 = new Composite(this, SWT.NONE);
		composite_1.setLayoutData(BorderLayout.CENTER);

	}

	/**
	 * Set loading
	 * 
	 * @param loading
	 */
	public void setLoading(boolean loading) {
		this.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (loading) {
					CompositeWithLoader.this.setupProgressBar(true);
					return;
				}

				CompositeWithLoader.this.setupProgressBar(false);
			}
		});

	}

	/**
	 * Creates the progressBar
	 * 
	 * @param indeterminated
	 */
	private void setupProgressBar(boolean indeterminated) {
		if (progressBar != null) {
			progressBar.dispose();
		}
		if (indeterminated) {
			progressBar = new ProgressBar(composite, SWT.NONE | SWT.INDETERMINATE);
		} else {
			progressBar = new ProgressBar(composite, SWT.NONE);
		}

		this.layout(true, true);
		this.getParent().update();
		this.getParent().layout(true, true);
	}

}
