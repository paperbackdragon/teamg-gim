package client;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Window;

import java.awt.SystemTray;

import java.awt.TrayIcon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import client.net.ClientConnection;
import client.net.ServerConnection;
import client.ui.*;

public class GimClient {

	private static MainWindow mainWindow;
	private static ArrayList<ChatWindowIdentifier> windows;
	private static Model model = Model.getInstance();

	private static ServerConnection inLink = new ServerConnection();
	private static ClientConnection outLink = new ClientConnection(inLink);

	private static SystemTray tray;
	private static TrayIcon trayIcon;

	public static void main(String[] args) {

		model.setOutLink(outLink);

		// rooms = new ArrayList<ChatPanel>();
		windows = new ArrayList<ChatWindowIdentifier>();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setUpTray();

				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
				}

				LoginPanel lp = new LoginPanel();
				mainWindow = new MainWindow("GIM", lp);
				lp.setParent(mainWindow);
				mainWindow.setVisible(true);
			}
		});

	}
	
	// DEBUG
	public static void printWindows() {
		if (windows.size() > 0) {
			for (int i = 0; i < windows.size(); i++) {
				System.out.println("window: " + windows.get(i).getUser());
			}
		}
		else {
			System.out.println("window list empty!");
		}
	}
	
	// </DEBUG>

	// ACCESSORS
	public static MainWindow getMainWindow() {
		return mainWindow;
	}

	// HELPER METHODS
	// public static void addRoom(ChatPanel panel) {
	// rooms.add(panel);
	// }

	public static void removeRoom(ChatPanel panel) {
		for (int i = 0; i < windows.size(); i++) {
			if (windows.get(i).getChatPanel().getID().equals(panel.getID())) {
				windows.remove(i);
			}
		}
	}

	// We do this so we don't open a new chat every time the user clicks chat
	public static int findRoom(User chatWith) {
		for (int i = 0; i < windows.size(); i++) {
			if (windows.get(i).getChatPanel() instanceof SingleChatPanel) {
				if (windows.get(i).getChatPanel().getChatWith() == chatWith) {
					// set chat to visible
					return i;
				}
			}
		}
		// not found

		return -1;
	}

	public static void routeMessage(String roomid, User sender, String message) {
		System.out.println("routing message");
		for (int i = 0; i < windows.size(); i++) {

			if (windows.get(i).getChatPanel().getID().equals(roomid)) {
				System.out.println("found the room " + roomid);

				windows.get(i).getChatPanel().receiveMessage(sender, message);
				Model.getInstance().setLatestPerson(roomid);
				break;
			}
		}

	}

	public static void addWindow(User user, JFrame ui, ChatPanel cp) {
		windows.add(new ChatWindowIdentifier(user, ui, cp));
	}

	public static JFrame getWindow(User user) {
		for (int i = 0; i < windows.size(); i++) {
			if (windows.get(i).getUser() == user) {
				return windows.get(i).getWindow();
			}
		}
		return null;
	}

	/* Used for group chat, where "user" is not applicable */
	public static JFrame getWindowRoom(String id) {
		for (int i = 0; i < windows.size(); i++) {
			if (windows.get(i).getChatPanel().getID().equals(id)) {
				return windows.get(i).getWindow();
			}
		}
		return null;
	}

	public static ChatWindowIdentifier getWindowIdentifierFromUser(User user) {
		for (int i = 0; i < windows.size(); i++) {
			if (windows.get(i).getChatPanel() instanceof SingleChatPanel) {
				if (windows.get(i).getChatPanel().getChatWith() == user) {
					return windows.get(i);
				}
			}

		}
		return null;

	}

	public static ChatWindowIdentifier getWindowIdentifierFromId(String roomid) {
		for (int i = 0; i < windows.size(); i++) {
			if (windows.get(i).getChatPanel().getID().equals(roomid)) {
				return windows.get(i);
			}
		}
		return null;
	}

	// called when user has logged out / loses connection , but still has
	// windows open...
	// if they logged back in, this could cause strange behaviour otherwise
	public static void resetRoomIds() {

		if (windows.size() > 0) {
			// look at me, using this shorthand for the first time :D
			for (ChatWindowIdentifier window : windows) {
				window.getChatPanel().setId("-1");
				window.getChatPanel().setInProgress(false);
			}
		}
	}

	public static void clearGroupChats() {

		if (windows.size() > 0) {
			for (ChatWindowIdentifier window : windows) {
				if (window.getChatPanel() instanceof GroupChatPanel) {
					((GroupChatPanel) window.getChatPanel()).clearUserList();
				}
			}
		}

	}

	public static void setChatBoxes(final Boolean b) {

		if (windows.size() > 0) {
			for (ChatWindowIdentifier window : windows) {
				
				// if it's a group chat, we don't want to re-enable
				if (!(window.getChatPanel() instanceof GroupChatPanel)) {
					window.getChatPanel().chatBoxEnabled(b);
				} else if (b == false) {
					window.getChatPanel().chatBoxEnabled(b);

				}

			}
		}
	}

	public static SystemTray getTray() {
		return tray;
	}

	public static TrayIcon getTrayIcon() {
		return trayIcon;
	}

	/**
	 * Called whenever a method might want to notify via a pop up from the
	 * system tray to check this is supported. Otherwise, a null pointer would
	 * occur.
	 */
	public static Boolean SystemTraySupported() {
		return SystemTray.isSupported();
	}

	/**
	 * Alerts the user they have received a message, in a window they are not
	 * focused on
	 */
	public static void alertMessage(String from, String message) {

		// Should handle null pointer in lab?
		if (SystemTraySupported() && model.getOptions().showNotifications) {
			getTrayIcon().displayMessage(from + " says: ", message, TrayIcon.MessageType.INFO);
		}

	}

	public static void setUpTray() {

		if (SystemTray.isSupported()) {

			tray = SystemTray.getSystemTray();
			Image image = new ImageIcon(model.getPath() + "status/offline.png").getImage();

			MouseListener mouseListener = new MouseListener() {

				public void mouseClicked(MouseEvent e) {
					System.out.println("Tray Icon - Mouse clicked!");

					GimClient.getMainWindow().setVisible(true);
				}

				public void mouseEntered(MouseEvent e) {
					System.out.println("Tray Icon - Mouse entered!");
				}

				public void mouseExited(MouseEvent e) {
					System.out.println("Tray Icon - Mouse exited!");
				}

				public void mousePressed(MouseEvent e) {
					System.out.println("Tray Icon - Mouse pressed!");
				}

				public void mouseReleased(MouseEvent e) {
					System.out.println("Tray Icon - Mouse released!");
				}
			};

			ActionListener exitListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Exiting...");
					System.exit(0);
				}
			};

			PopupMenu popup = new PopupMenu();
			MenuItem defaultItem = new MenuItem("Exit");
			defaultItem.addActionListener(exitListener);
			popup.add(defaultItem);

			image = image.getScaledInstance((int) tray.getTrayIconSize().getWidth(), (int) tray.getTrayIconSize()
					.getHeight(), Image.SCALE_SMOOTH);
			trayIcon = new TrayIcon(image, "GIM", popup);

			ActionListener actionListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					/*
					 * trayIcon.displayMessage("Action Event",
					 * "An Action Event Has Been Performed!",
					 * TrayIcon.MessageType.INFO);
					 */

					if (e.getSource() == trayIcon) {
						// Bring window to front. NEED TO WORK OUT HOW TO DO
						// THIS

						if (Model.getInstance().getLatestPerson() != null) {
							GimClient.getWindowIdentifierFromId(Model.getInstance().getLatestPerson()).getWindow()
									.setVisible(true);
						}
					}

				}
			};

			trayIcon.setImageAutoSize(true);
			trayIcon.addActionListener(actionListener);
			trayIcon.addMouseListener(mouseListener);

			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				System.err.println("TrayIcon could not be added.");
			}

		} else {

			// System Tray is not supported

		}

		// redundant... i think. i'm not certain, yet!
		/*
		 * public static void removeWindowIdentifier(String roomid) {
		 * 
		 * for (int i =0; i < windows.size(); i ++) { if
		 * (windows.get(i).getId().equals(roomid)) { windows.remove(i); } } }
		 */
	}

}
