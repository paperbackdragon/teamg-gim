package client.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;

import client.GimClient;

public class ContactPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private MainWindow parent;
	private JButton add, del, chat, group;
	private JTree contactTree;
	private DefaultMutableTreeNode contacts;
	private ClientModel model = ClientModel.getInstance();
	private PersonalInfo info;

	// private chatListener;

	// CONSTRUCTOR
	public ContactPanel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		contacts = new DefaultMutableTreeNode("Contacts");
		
		setLayout(new BorderLayout());
		info = new PersonalInfo();
		add(info, BorderLayout.NORTH);
		add(new JScrollPane(new ContactList()), BorderLayout.CENTER);
		add(new ButtonPanel(), BorderLayout.SOUTH);
	}

	
	// PANELS
	private class PersonalInfo extends JPanel {
		private JButton name; 
		private JLabel message;
		private JLabel status;
		
		class TextField extends JPanel {

			public TextField() {
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				name = new JButton();
				message = new JLabel("Personal Message");
				status = new JLabel("<html><font size=\"3\">Status: Online</font></html>");
				add(name);
				add(message);
				add(status);
			}
		}

		public PersonalInfo() {
			setLayout(new FlowLayout(FlowLayout.LEFT));
			ImageIcon icon = createImageIcon("icon1.jpg", "Icon");
			JLabel iconLabel = new JLabel(icon);
			iconLabel.setPreferredSize(new Dimension(64, 64));
			iconLabel.setBorder(BorderFactory.createLineBorder(Color.black));
			add(iconLabel);
			add(new TextField());
		}

		protected ImageIcon createImageIcon(String path, String description) {
			java.net.URL imgURL = getClass().getResource(path);
			if (imgURL != null) {
				return new ImageIcon(imgURL, description);
			} else {
				System.err.println("Couldn't find file: " + path);
				return null;
			}
		}
		
		public void setNickname(String name) {
			this.name.setText(name);

		}

		public void setStatus(String status) {
			this.status.setText(status);
		}

		public void setPersonalMessage(String message) {
			this.message.setText(message);
		}
	}
	
	public void setMyNickname(String name) {
		info.setNickname(name);
	}
	
	public void setMyPersonalMessage(String message) {
		info.setPersonalMessage(message);
	}
	
	public void setMyStatus(String status) {
		info.setStatus(status);
	}


	class ContactList extends JPanel {
		public ContactList() {
			setLayout(new BorderLayout());

			contactTree = new JTree(contacts);
			contactTree.addMouseListener(new SingleChatListener());
			contactTree.setRootVisible(false);
			contactTree.setShowsRootHandles(true);

			add(contactTree, BorderLayout.CENTER);
		}
	}

	class ButtonPanel extends JPanel {
		public ButtonPanel() {
			setLayout(new GridLayout(1, 4, 5, 5));
			add = new JButton("ADD");
			del = new JButton("DEL");
			chat = new JButton("CHAT");
			group = new JButton("GROUP");

			ButtonListener buttonListener = new ButtonListener();
			add.addActionListener(buttonListener);
			del.addActionListener(buttonListener);
			chat.addActionListener(buttonListener);
			group.addActionListener(buttonListener);

			add(add);
			add(del);
			add(chat);
			add(group);
		}
	}

	// HELPER METHODS
	public Dimension getPreferredSize() {
		return new Dimension(300, 400);
	}

	public void setParent(JFrame frame) {
		parent = (MainWindow) frame;
	}

	public void createNodes(String[] online, String[] offline) {
		// TODO (heather): change node icons (see java tutorial)

		contacts.removeAllChildren();

		DefaultMutableTreeNode status = null;
		DefaultMutableTreeNode contact = null;

		status = new DefaultMutableTreeNode("Online");
		contacts.add(status);

		// set online contacts
		for (String str : online) {
			contact = new DefaultMutableTreeNode(str);
			status.add(contact);
		}

		status = new DefaultMutableTreeNode("Offline");
		contacts.add(status);

		// set offline contacts
		for (String str : offline) {
			contact = new DefaultMutableTreeNode(str);
			status.add(contact);
		}

		((DefaultTreeModel) contactTree.getModel()).reload();
		for (int i = 0; i < contactTree.getRowCount(); i++)
			contactTree.expandRow(i);
	}

	private String[] getSelectedContacts() {
		TreePath[] nodes = contactTree.getSelectionPaths();
		DefaultMutableTreeNode node;
		String[] contacts = new String[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			node = (DefaultMutableTreeNode) nodes[i].getLastPathComponent();
			contacts[i] = (String) node.getUserObject();
			System.out.println((String) node.getUserObject());
		}
		return contacts;
	}
	


	// ACTION LISTENERS
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(add)) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						/*
						 * GimUI ui = new GimUI("GIM - Find Contact", new
						 * FindPopup()); ui.setVisible(true);
						 * ui.setLocationRelativeTo(null);// center new chat
						 * window
						 */

						Object[] possibilities = null;
						String s = (String) JOptionPane.showInputDialog(null,
								"Input the user name of the person you wish to add to your contacts", "Add afriend",
								JOptionPane.PLAIN_MESSAGE, null, null, "ham");

						if (s != null) {
							GimClient.getClient().addfriend(s);
						}

						GimClient.getClient().getFriendList();

					}
				});
			} else if (e.getSource().equals(del)) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						/*
						 * GimUI ui = new GimUI("GIM - Remove Contact", new
						 * RemovePopup());
						 * ui.setLocationRelativeTo(null);//center new chat
						 * window ui.setVisible(true);
						 */

						// construct string
						String theString = "";
						for (int i = 0; i < getSelectedContacts().length; i++) {
							theString += getSelectedContacts()[i] + ", ";
						}

						Object[] options = { "Yes", "No", };
						int n = JOptionPane
								.showOptionDialog(
										null,
										"Are you sure you want to remove: "
												+ theString
												+ " from your contacts? (note, for current testing purposes this function is disallowed and will not work for now)",
										"remove friends", JOptionPane.YES_NO_CANCEL_OPTION,
										JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

						if (n == 0) {
							System.out.println("yes");

							for (int i = 0; i < getSelectedContacts().length; i++) {
								// for now, disallow
								// GimClient.getClient().removefriend(getSelectedContacts()[i]);
							}

							// update friendslist
							GimClient.getClient().getFriendList();

						}
					}
				});

			}
			// TODO (heather): make chat grayed out till someone is clicked
			else if (e.getSource().equals(chat)) {
				// Check if there's already a chat open with this user
				int find = GimClient.findRoom(getSelectedContacts()[0]);
				if (find == -1) {
					GimClient.getClient().createRoom(false, getSelectedContacts());
				} else {
					GimClient.getWindow(getSelectedContacts()[0]).setVisible(true);
				}
			} else if (e.getSource().equals(group)) {
				GimClient.getClient().createRoom(true, getSelectedContacts());
			}
		}
	}

	class SingleChatListener implements MouseListener {
		public void mousePressed(MouseEvent e) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) contactTree.getLastSelectedPathComponent();
			String nodeInfo = (String) node.getUserObject();
			if (e.getClickCount() == 2) {
				if (nodeInfo != "Online" && nodeInfo != "Offline") {
					System.out.println(nodeInfo);
					int find = GimClient.findRoom(getSelectedContacts()[0]);
					if (find == -1) {
						GimClient.getClient().createRoom(false, getSelectedContacts());
					} else {
						GimClient.getWindow(getSelectedContacts()[0]).setVisible(true);
					}
				}
			}
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	}
}