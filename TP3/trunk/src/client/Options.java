package client;

import java.io.Serializable;

/**
 * Oh yeah baby!
 * 
 * @author james
 * 
 */
public class Options implements Serializable {

	private static final long serialVersionUID = 6L;
	
	boolean remeberEmail;
	boolean rememberPassword;
	boolean autoLogin;
	String email;
	String password;
	
	public int chatWindowWidth = 650;
	public int chatWindowHeight = 500;
	
	public int mainWindowWidth = 310;
	public int mainWindowHeight = 600;
	
	public boolean showOffline = false;
	public boolean enableLogging = true;
	public boolean showTimestamps = true;
	
	public boolean showNotifications = true;

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
