package client.ui;

public class chatWindowIdentifier {
	
	//String id;
	String user;
	GimUI window;
	ChatPanel cp;
	
	public chatWindowIdentifier(String user, GimUI window, ChatPanel cp) {
		this.user = user;
		this.window = window;
		this.cp = cp;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public ChatPanel getCp() {
		return cp;
	}

	public void setGcp(ChatPanel cp) {
		this.cp = cp;
	}

	public GimUI getWindow() {
		return window;
	}

	public void setWindow(GimUI window) {
		this.window = window;
	}
	
	

}
