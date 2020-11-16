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
		progressBar = new ProgressBar(this, SWT.NONE);
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

	public void setText(String text) {
		labelInfo.setText(text);
	}

	public void setProgress(int progress) {
		progressBar.setSelection((progress) % (progressBar.getMaximum()));
	}

	public void autoProgressBar() {
		new Thread(new Runnable() {
			private int progress = 0;
			private static final int INCREMENT = Config.INCREMENT_AUTO_PROGRESSBAR_AMOUNT;

			@Override
			public void run() {
				while (!progressBar.isDisposed()) {
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							if (progressBar.isDisposed())
								return;
							if (progressBar.getMaximum() >= progress) {
								progressBar
										.setSelection((progress += INCREMENT) % (progressBar.getMaximum() + INCREMENT));
							}
						}
					});

					try {
						Thread.sleep(Config.INTERVAL_AUTO_PROGRESSBAR_MS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
