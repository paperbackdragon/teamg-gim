package client.ui;

public class chatWindowIdentifier {
	
	//String id;
	String user;
	MainWindow window;
	ChatPanel cp;
	
	public chatWindowIdentifier(String user, MainWindow window, ChatPanel cp) {
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

	public MainWindow getWindow() {
		return window;
	}

	public void setWindow(MainWindow window) {
		this.window = window;
	}

}
