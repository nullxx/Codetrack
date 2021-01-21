package codetrack;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Config {

	// temporal
	public final static String TABBAR_DEFAULT_TITLE = "CODETRACK";
	public final static int INTERVAL_AUTO_PROGRESSBAR_MS = 200;
	public final static int INCREMENT_AUTO_PROGRESSBAR_AMOUNT = 50;
	public final static String PLUGIN_ID = "codetrack";
	public final static String API_BASE_URL = "https://codetrack.nullx.me";
	public final static String LOGIN_EMAIL_TEXTFIELD_INITIAL_VALUE = "test@codetrack.org";
	public final static String LOGIN_PASSWORD_TEXTFIELD_INITIAL_VALUE = "patata123";
	public final static String LOGIN_DETAIL_MESSAGE = "Introduce tus credenciales";
	public final static String LOGIN_SEND_EMAIL_BUTTON_TEXT = "Enviar";
	public final static String TAB_PERMISSIONS_FOLDER_NAME = "Permisos";
	
	public final static String LOCAL_STORAGE_KEY_BEARER_TOKEN = "bearerToken";

	public final static String LOCAL_PROJECT_FILE_INFO = ".codetrack.json";

	public final static String REMOTE_RESPONSE_KEY_CODE = "code";
	public final static String REMOTE_RESPONSE_KEY_DATA = "data";
	public final static String REMOTE_RESPONSE_KEY_ERROR = "error";
	public final static String REMOTE_RESPONSE_KEY_TOKEN = "token";

	public final static Charset HTTP_CHARSET = StandardCharsets.UTF_8;
	public final static String HTTP_HEADER_BEARER_TOKEN = "Bearer %s";
	
	public final static String CLIENT_BASE_URL = "http://127.0.0.1:5500"; // 
}
