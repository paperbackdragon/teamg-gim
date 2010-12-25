package client.ui;

public class chatWindowIdentifier {
	
	String id;
	String user;
	GimUI window;
	ChatPanel cp;
	
	public chatWindowIdentifier(String user, String id, GimUI window, ChatPanel cp) {
		this.user = user;
		this.id = id;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public GimUI getWindow() {
		return window;
	}

	public void setWindow(GimUI window) {
		this.window = window;
	}
	
	

}
