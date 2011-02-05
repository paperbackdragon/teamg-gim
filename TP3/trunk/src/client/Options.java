package client;

import java.io.Serializable;

/**
 * Oh yeah baby!
 * 
 * @author james
 * 
 */
public class Options implements Serializable {

	private static final long serialVersionUID = 1L;
	boolean remeberEmail;
	boolean rememberPassword;
	boolean autoLogin;
	String email;
	String password;

	public boolean isRemeberEmail() {
		return remeberEmail;
	}

	public void setRemeberEmail(boolean remeberEmail) {
		this.remeberEmail = remeberEmail;
	}

	public boolean isRememberPassword() {
		return rememberPassword;
	}

	public void setRememberPassword(boolean rememberPassword) {
		this.rememberPassword = rememberPassword;
	}

	public boolean isAutoLogin() {
		return autoLogin;
	}

	public void setAutoLogin(boolean autoLogin) {
		this.autoLogin = autoLogin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
