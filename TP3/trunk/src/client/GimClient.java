package client;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

import client.ui.*;

public class GimClient {

	private static MainWindow mainWindow;
	private static ClientModel client;
	private static ContactPanel contactPanel;
	// private static ArrayList<ChatPanel> rooms;
	private static ArrayList<chatWindowIdentifier> windows;

	private static SystemTray tray;
	static TrayIcon trayIcon;

	public static void main(String[] args) {
		client = ClientModel.getInstance();

		// rooms = new ArrayList<ChatPanel>();
		windows = new ArrayList<chatWindowIdentifier>();
		contactPanel = new ContactPanel();

		setUpTray();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LoginPanel lp = new LoginPanel();
				mainWindow = new MainWindow("GIM", lp);
				lp.setParent(mainWindow);
				mainWindow.setVisible(true);
			}
		});
	}

	// ACCESSORS
	public static MainWindow getMainWindow() {
		return mainWindow;
	}

	public static ClientModel getClient() {
		return client;
	}

	public static ContactPanel getContactPanel() {
		return contactPanel;
	}

	// HELPER METHODS
	// public static void addRoom(ChatPanel panel) {
	// rooms.add(panel);
	// }

	public static void removeRoom(ChatPanel panel) {
		for (int i = 0; i < windows.size(); i++) {
			if (windows.get(i).getCp().getID().equals(panel.getID())) {
				windows.remove(i);
				// TODO (heather): Some way to reduce memory used in this list?
			}
		}
	}

	// We do this so we don't open a new chat every time the user clicks chat
	public static int findRoom(String chatWith) {
		for (int i = 0; i < windows.size(); i++) {
			if (windows.get(i).getCp() instanceof SingleChatPanel) {
				if (windows.get(i).getCp().getChatWith().equals(chatWith)) {
					// set chat to visible
					return i;
				}
			}
		}
		// not found

		return -1;
	}

	public static void routeMessage(String roomid, String sender, String message) {
		System.out.println("routing message");
		for (int i = 0; i < windows.size(); i++) {
			System.out.println("found the room");
			if (windows.get(i).getCp().getID().equals(roomid)) {
				windows.get(i).getCp().receiveMessage(sender, message);
				GimClient.getClient().setLatestPerson(roomid);
				break;
			}
		}

	}

	public static void addWindow(String user, MainWindow ui, ChatPanel cp) {
		windows.add(new chatWindowIdentifier(user, ui, cp));
	}

	public static MainWindow getWindow(String user) {
		for (int i = 0; i < windows.size(); i++) {
			if (windows.get(i).getUser().equals(user)) {
				return windows.get(i).getWindow();
			}
		}
		return null;
	}

	/* Used for group chat, where "user" is not applicable */
	public static MainWindow getWindowRoom(String id) {
		for (int i = 0; i < windows.size(); i++) {
			if (windows.get(i).getCp().getID().equals(id)) {
				return windows.get(i).getWindow();
			}
		}
		return null;
	}

	public static chatWindowIdentifier getWindowIdentifierFromUser(String user) {
		for (int i = 0; i < windows.size(); i++) {
			if (windows.get(i).getCp() instanceof SingleChatPanel) {
				if (windows.get(i).getCp().getChatWith().equals(user)) {
					return windows.get(i);
				}
			}

		}
		return null;

	}

	public static chatWindowIdentifier getWindowIdentifierFromId(String roomid) {
		for (int i = 0; i < windows.size(); i++) {
			if (windows.get(i).getCp().getID().equals(roomid)) {
				return windows.get(i);
			}
		}
		return null;
	}

	public static SystemTray getTray() {
		return tray;
	}

	public static TrayIcon getTrayIcon() {
		return trayIcon;
	}

	public static void setUpTray() {

		if (SystemTray.isSupported()) {

			tray = SystemTray.getSystemTray();
			Image image = Toolkit.getDefaultToolkit().getImage(
					"smiles/Happy_smiley.png");

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

			trayIcon = new TrayIcon(image, "Tray Demo", popup);

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

						if (GimClient.getClient().getLatestPerson() != null) {
							GimClient.getWindowIdentifierFromId(
									GimClient.getClient().getLatestPerson())
									.getWindow().setVisible(true);
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
