package client.ui;

import client.User;

public class ChatWindowIdentifier {
	
	User user;
	MainWindow window;
	ChatPanel cp;
	
	public ChatWindowIdentifier(User user, MainWindow window, ChatPanel cp) {
		this.user = user;
		this.window = window;
		this.cp = cp;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ChatPanel getChatPanel() {
		return cp;
	}

	public void setChatPanel(ChatPanel cp) {
		this.cp = cp;
	}

	public MainWindow getWindow() {
		return window;
	}

	public void setWindow(MainWindow window) {
		this.window = window;
	}

}
