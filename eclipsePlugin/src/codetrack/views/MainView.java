package codetrack.views;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Composite;

import codetrack.api.RestAPI;

public class MainView extends Composite {
	private Text txtPassword;
	private Text txtEmail;
    private List logList; 

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public MainView(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));

		TabFolder tabFolder = new TabFolder(this, SWT.NONE);

		TabItem tbtmLogin = new TabItem(tabFolder, SWT.NONE);
		tbtmLogin.setToolTipText("");
		tbtmLogin.setText("Usuario");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmLogin.setControl(composite);
		composite.setLayout(new FormLayout());

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		FormData fd_composite_1 = new FormData();
		fd_composite_1.top = new FormAttachment(0, 29);
		fd_composite_1.bottom = new FormAttachment(100);
		fd_composite_1.left = new FormAttachment(0);
		fd_composite_1.right = new FormAttachment(100);
		composite_1.setLayoutData(fd_composite_1);

		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setLayout(new FormLayout());

		Label lblIntroduceTusCredenciales = new Label(composite_2, SWT.NONE);
		FormData fd_lblIntroduceTusCredenciales = new FormData();
		fd_lblIntroduceTusCredenciales.right = new FormAttachment(0, 205);
		fd_lblIntroduceTusCredenciales.top = new FormAttachment(0, 10);
		fd_lblIntroduceTusCredenciales.left = new FormAttachment(0, 10);
		lblIntroduceTusCredenciales.setLayoutData(fd_lblIntroduceTusCredenciales);
		lblIntroduceTusCredenciales.setText("Introduce tus credenciales");

		Composite composite_4 = new Composite(composite_2, SWT.NONE);
		composite_4.setLayout(new FillLayout(SWT.VERTICAL));
		FormData fd_composite_4 = new FormData();
		fd_composite_4.top = new FormAttachment(lblIntroduceTusCredenciales, 6);
		fd_composite_4.bottom = new FormAttachment(100);
		fd_composite_4.left = new FormAttachment(0, 10);
		fd_composite_4.right = new FormAttachment(0, 205);
		composite_4.setLayoutData(fd_composite_4);

		txtEmail = new Text(composite_4, SWT.BORDER);
		txtEmail.setText("email");

		txtPassword = new Text(composite_4, SWT.BORDER);
		txtPassword.setText("password");

		Button btnEnviar = new Button(composite_4, SWT.NONE);
		btnEnviar.setText("Enviar");

		btnEnviar.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				MainView.this.toggleLogin();
			}
		});

		Composite composite_3 = new Composite(composite_1, SWT.NONE);
		composite_3.setLayout(new FillLayout(SWT.VERTICAL));
		
		logList = new List(composite_3, SWT.BORDER);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.bottom = new FormAttachment(composite_1, -6);
		fd_lblNewLabel.left = new FormAttachment(0, 185);
		fd_lblNewLabel.top = new FormAttachment(0, 9);
		fd_lblNewLabel.right = new FormAttachment(100, -186);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("USUARIO");
		
		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Proyectos");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private void toggleLogin() {
		log("Sending login");
		try {
			boolean logged = RestAPI.login(txtEmail.getText(), txtPassword.getText());
			if (logged) {
				log("Login true!!!!");
			} else {
				log("Login false");
			}
		} catch (IOException | InterruptedException e) {
			log(String.format("Error: %s", e.getMessage()));
			e.printStackTrace(); // TODO error handling
		}
	}
	private void log(String msg) {
		logList.add(msg);
		logList.select(logList.getItemCount() - 1);
		logList.showSelection();
	}
}
