package codetrack.client;

import java.io.IOException;
import java.util.HashMap;

import codetrack.api.Crypto;


public class User {
	private String email;
	private String password;


	public User(String email, String password) throws IOException {
		this.setEmail(email);
		this.setPassword(password);
	}

	/**
	 * @return String
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param user
	 * @throws IOException
	 */
	public void setEmail(String email) throws IOException {
		if (!email.isEmpty()) {
			this.email = email;
		} else {
			throw new IOException("Email cannot be empty");
		}

	}

	/**
	 * @return String
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 * @throws IOException
	 */
	public void setPassword(String password) throws IOException {
		if (!password.isEmpty()) {
			this.password = password;
		} else {
			throw new IOException("User can't be empty");
		}

	}

	/**
     * Get user data formated in a HashMap
     * 
     * @return HashMap<String, String>
     */
    public HashMap<String, Object> getFormated() {
    	HashMap<String, Object> user = new HashMap<>();
    	user.put("email", this.email);
    	user.put("password", Crypto.genSHA512(this.password));
    	return user;
    }
}