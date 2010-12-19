package client;

import java.util.ArrayList;
import javax.swing.SwingUtilities;

import client.ui.*;

public class GimClient {
	private static GimUI mainWindow;
	private static ClientModel client;
	private static ArrayList<ChatPanel> rooms;
	
	public static void main(String[] args) {
		client = new ClientModel();
		rooms = new ArrayList<ChatPanel>();
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LoginPanel lp = new LoginPanel();
				mainWindow = new GimUI("GIM", lp);
				lp.setParent(mainWindow);
			}
		});
	}
	
	//ACCESSORS
	public static GimUI getMainWindow() {
		return mainWindow;
	}
	
	public static ClientModel getClient() {
		return client;
	}
	
	//HELPER METHODS
	public static void addRoom(ChatPanel panel) {
		rooms.add(panel);
	}
	
	public static void removeRoom(ChatPanel panel) {
		for(int i=0; i < rooms.size(); i++) {
			if(rooms.get(i).getID().equals(panel.getID())) {
				rooms.remove(i);
				// TODO some way to reduce memory used in this list?
			}
		}
	}
}
