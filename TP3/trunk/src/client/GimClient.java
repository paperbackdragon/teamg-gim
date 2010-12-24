package client;

import java.util.ArrayList;
import javax.swing.SwingUtilities;

import client.ui.*;

public class GimClient {
	private static GimUI mainWindow;
	private static ClientModel client;
	private static ContactPanel contactPanel;
	private static ArrayList<ChatPanel> rooms;
	private static ArrayList<chatWindowIdentifier> windows;
	
	public static void main(String[] args) {
		client = new ClientModel();
		rooms = new ArrayList<ChatPanel>();
		windows = new ArrayList<chatWindowIdentifier>();
		contactPanel = new ContactPanel();
		
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
	
	public static ContactPanel getContactPanel() {
		return contactPanel;
	}
	
	//HELPER METHODS
	public static void addRoom(ChatPanel panel) {
		rooms.add(panel);
	}
	
	public static void removeRoom(ChatPanel panel) {
		for(int i=0; i < rooms.size(); i++) {
			if(rooms.get(i).getID().equals(panel.getID())) {
				rooms.remove(i);
				//TODO (heather): Some way to reduce memory used in this list?
			}
		}
	}
	
	// We do this so we don't open a new chat every time the user clicks chat
	public static int findRoom(String chatWith) {
		for (int i = 0; i < rooms.size(); i++ ) {
			if (rooms.get(i).getChatWith().equals(chatWith)) {
				// set chat to visible
				return i;
			}
		}
		// not found
		
		return -1;
	}
	
	
	public static void routeMessage(String roomid, String sender, String message) {
		System.out.println("routing message");
		for (int i=0; i< rooms.size(); i++) {
			System.out.println("found the room");
			if (rooms.get(i).getID().equals(roomid)) {
				rooms.get(i).receiveMessage(sender, message);
				
				break;
			}
		}
		
	}

	public static void addWindow(String roomid, GimUI ui) {
		windows.add(new chatWindowIdentifier(roomid, ui));
	}
	
	public static GimUI getWindow(String roomid) {
		for (int i = 0; i < windows.size(); i++ ) {
			if (windows.get(i).getId().equals(roomid)) {
				return windows.get(i).getWindow();
			}
		}
		return null;
	}
}
