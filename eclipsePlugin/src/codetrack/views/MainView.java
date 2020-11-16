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

import codetrack.Config;
import codetrack.Storage;
import codetrack.api.RestAPI;
import codetrack.client.Project;
import codetrack.listeners.Listeners;
import swing2swt.layout.BorderLayout;

public class MainView extends CompositeWithLoader {
	private Text txtPassword;
	private Text txtEmail;
	private List logList;
	private TabFolder tabFolder;
	private Permissions tbtmPermisos;
	private Composite composite_2;

	public MainView(Composite parent, int style) {
		super(parent, style);

		tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setLayoutData(BorderLayout.CENTER);

		TabItem tbtmLogin = new TabItem(tabFolder, SWT.NONE);
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

		composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setLayout(new BorderLayout(0, 0));

		Label lblIntroduceTusCredenciales = new Label(composite_2, SWT.NONE);
		lblIntroduceTusCredenciales.setLayoutData(BorderLayout.NORTH);
		lblIntroduceTusCredenciales.setText(Config.LOGIN_DETAIL_MESSAGE);

		Composite composite_4 = new Composite(composite_2, SWT.NONE);
		composite_4.setLayout(new FillLayout(SWT.VERTICAL));

		txtEmail = new Text(composite_4, SWT.BORDER);
		txtEmail.setText(Config.LOGIN_EMAIL_TEXTFIELD_INITIAL_VALUE);

		txtPassword = new Text(composite_4, SWT.BORDER | SWT.PASSWORD);
		txtPassword.setText(Config.LOGIN_PASSWORD_TEXTFIELD_INITIAL_VALUE);

		Button btnEnviar = new Button(composite_4, SWT.NONE);
		btnEnviar.setText(Config.TAB_PERMISSIONS_FOLDER_NAME);

		btnEnviar.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				MainView.this.toggleLogin(txtEmail.getText(), txtPassword.getText());
			}
		});

		Composite composite_3 = new Composite(composite_1, SWT.NONE);
		composite_3.setLayout(new FillLayout(SWT.VERTICAL));

		logList = new List(composite_3, SWT.BORDER);

		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.bottom = new FormAttachment(composite_1, -6);
		fd_lblNewLabel.left = new FormAttachment(0, 185);
		fd_lblNewLabel.top = new FormAttachment(0, 9);
		fd_lblNewLabel.right = new FormAttachment(100, -186);

		this.autoLogIn();
		this.initListeners();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private void initListeners() {
		try {
			new Listeners();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void openPermisos() {
		if (this.tbtmPermisos != null)
			return;
		this.tbtmPermisos = new Permissions(this.tabFolder, SWT.BORDER, Config.TAB_PERMISSIONS_FOLDER_NAME);
	}

	private void toggleLogin(String email, String password) {

		log("Sending login");
		this.setLoading("Sending login", true);
		String message = null;
		try {
			boolean logged = RestAPI.login(email, password);
			if (logged) {
				log("Login success");
				message = "Success login";

				openPermisos();
				hideLogin();
				initLogged();

				this.tabFolder.pack();
				getParent().layout(true, true);
			} else {
				log("Login unsuccessfull");
				message = "Please check your email || password";
			}
		} catch (IOException | InterruptedException e) {
			log(message);
			message = this.handleException(e);
		}
		this.setLoading(message, false);
	}

	private void autoLogIn() {
		log("LAUNCHING AUTO-LOGIN");
		Storage s = new Storage();
		try {
			String bearer = s.getString(Config.LOCAL_STORAGE_KEY_BEARER_TOKEN);
			if (bearer != null) {
				log("Login success");

				openPermisos();
				hideLogin();
				initLogged();

				this.tabFolder.pack();
				getParent().layout(true, true);
			}
		} catch (IOException | InterruptedException e) {
			String message = this.handleException(e);
			this.setError(message);
		}

	}

	public void initLogged() throws IOException, InterruptedException {
		Project.processProjects();
	}

	private String handleException(Exception e) {
		e.printStackTrace();
		String message = String.format("Error: %s", e.getMessage());
		return message;
	}

	private void hideLogin() {
		this.composite_2.dispose();
	}

	private void log(String msg) { // FIXME this is temporal logging !!! 
		logList.add(msg);
		logList.select(logList.getItemCount() - 1);
		logList.showSelection();
	}
}
