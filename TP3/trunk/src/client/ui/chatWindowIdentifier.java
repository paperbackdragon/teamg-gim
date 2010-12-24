package client.ui;

public class chatWindowIdentifier {
	
	String id;
	String user;
	GimUI window;
	
	public chatWindowIdentifier(String user, String roomid, GimUI window) {
		this.user = user;
		this.id = id;
		this.window = window;
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
