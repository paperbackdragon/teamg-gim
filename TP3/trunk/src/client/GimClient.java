package client;

import java.util.ArrayList;
import javax.swing.SwingUtilities;

import client.ui.*;

public class GimClient {
	private static GimUI mainWindow;
	private static ClientModel client;
	private ArrayList<ChatPanel> rooms;
	
	public static GimUI getMainWindow() {
		return mainWindow;
	}
	
	public static ClientModel getClient() {
		return client;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LoginPanel lp = new LoginPanel();
				mainWindow = new GimUI("GIM", lp);
				lp.setParent(mainWindow);
			}
		});
	}
}
