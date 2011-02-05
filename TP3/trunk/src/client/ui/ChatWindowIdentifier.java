package client.ui;

import javax.swing.JFrame;

import client.User;

public class ChatWindowIdentifier {
	
	User user;
	JFrame window;
	ChatPanel cp;
	
	public ChatWindowIdentifier(User user, JFrame ui, ChatPanel cp) {
		this.user = user;
		this.window = ui;
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

	public JFrame getWindow() {
		return window;
	}

	public void setWindow(ChatWindow window) {
		this.window = window;
	}

}
