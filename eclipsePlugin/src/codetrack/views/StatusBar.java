package codetrack.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.wb.swt.SWTResourceManager;

import codetrack.Config;

public class StatusBar extends Composite {

	private CLabel labelInfo;
	private ProgressBar progressBar;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public StatusBar(Composite parent, int style) {
		super(parent, style);
		
		setLayout(new FormLayout());
		this.setupProgressBar(false);
		progressBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_progressBar = new FormData();
		fd_progressBar.right = new FormAttachment(100, -10);
		fd_progressBar.top = new FormAttachment(0, 24);
		fd_progressBar.bottom = new FormAttachment(100, -24);
		progressBar.setLayoutData(fd_progressBar);

		labelInfo = new CLabel(this, SWT.NONE);
		labelInfo.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		// labelInfo.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 27, SWT.NORMAL));
		fd_progressBar.left = new FormAttachment(labelInfo, 6);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.right = new FormAttachment(100, -156);
		fd_lblNewLabel.left = new FormAttachment(0);
		fd_lblNewLabel.top = new FormAttachment(0);
		fd_lblNewLabel.bottom = new FormAttachment(100);
		labelInfo.setLayoutData(fd_lblNewLabel);

		this.setText(Config.TABBAR_DEFAULT_TITLE);

	}

	/**
	 * Set the status text
	 * @param text
	 */
	public void setText(String text) {
		labelInfo.setText(text);
	}

	/**
	 * Set loading
	 * @param loading
	 */
	public void setLoading(boolean loading) {
		if (loading) {
			this.setupProgressBar(true);
			return;
		}
		
		this.setupProgressBar(false);
	}
	
	/**
	 * Creates the progressBar
	 * @param indeterminated
	 */
	private void setupProgressBar(boolean indeterminated) {
		if (progressBar != null) {
			progressBar.dispose();
		}
		if (indeterminated) {
			progressBar = new ProgressBar(this, SWT.NONE | SWT.INDETERMINATE);
		} else {
			progressBar = new ProgressBar(this, SWT.NONE);
		}

		FormData fd_progressBar = new FormData();
		fd_progressBar.right = new FormAttachment(100, -10);
		fd_progressBar.top = new FormAttachment(0, 24);
		fd_progressBar.bottom = new FormAttachment(100, -24);
		fd_progressBar.left = new FormAttachment(labelInfo, 6);

		progressBar.setLayoutData(fd_progressBar);
		this.update();
		this.layout();
		progressBar.setParent(this);
		this.layout(true, true);
		this.getParent().update();
		this.getParent().layout(true, true);
	}
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
