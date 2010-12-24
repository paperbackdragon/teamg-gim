package client.ui;

public class chatWindowIdentifier {
	
	String id;
	GimUI window;
	
	public chatWindowIdentifier(String id, GimUI window) {
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
