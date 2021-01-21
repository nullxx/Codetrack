
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.HashMap;

import codetrack.api.Crypto;

import org.junit.Test;

import codetrack.api.RestAPI;
import codetrack.client.User;

public class UserTest {
	private final String email = "test@codetrack.org";
	private final String password = "patata123";

	@Test
	public void doLogin() {
		User user = null;
		try {
			user = RestAPI.login(email, password);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		assertFalse(user == null);
	}

	@Test
	public void checkEmpty() {
		Exception e1 = null;
		Exception e2 = null;
		try {
			new User("", password);
		} catch (IOException e) {
			e1 = e;
		}

		try {
			new User(email, "");
		} catch (IOException e) {
			e2 = e;
		}

		assertTrue(e1 instanceof IOException);
		assertTrue(e2 instanceof IOException);

	}

	@Test
	public void getFormatted() throws IOException {
		User user = new User(email, password);
		HashMap<String, Object> userFormatted = new HashMap<>();
		userFormatted.put("email", this.email);
		userFormatted.put("password", Crypto.genSHA512(this.password));
		assertEquals(user.getFormated(), userFormatted);
	}
}
