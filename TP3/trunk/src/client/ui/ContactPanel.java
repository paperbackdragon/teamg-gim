package client.ui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.*;

import client.GimClient;

public class ContactPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private MainWindow parent;
	private JButton add, del, chat, group;
	private JTree contactTree;
	protected JScrollPane scrollPane;
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
		scrollPane = new JScrollPane(new ContactList());
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);
		add(new ButtonPanel(), BorderLayout.SOUTH);
	}

	// PANELS
	private class PersonalInfo extends JPanel {

		private static final long serialVersionUID = 1L;

		private JButton name;
		private JLabel message;
		private JLabel status;

		class TextField extends JPanel {

			private static final long serialVersionUID = 1L;

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
			iconLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
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

		private static final long serialVersionUID = 1L;

		public ContactList() {
			setLayout(new BorderLayout());

			contactTree = new JTree(contacts);

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					contactTree.setCellRenderer(new CellRenderer());
				}
			});

			contactTree.putClientProperty("JTree.lineStyle", "None");
			
			contactTree.setLargeModel(true);
			contactTree.setRootVisible(false);
			contactTree.setShowsRootHandles(true);
			contactTree.addMouseListener(new SingleChatListener());

			add(contactTree, BorderLayout.CENTER);
		}
	}

	class ButtonPanel extends JPanel {

		private static final long serialVersionUID = 1L;

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

	/**
	 * Contact List Cell Renderer
	 * 
	 * @author James McMinn
	 * 
	 */
	private class CellRenderer implements TreeCellRenderer {

		private JPanel contact;

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {

			contact = new JPanel(new BorderLayout(5, 0));
			contact.setOpaque(false);

			// Don't show empty cells
			if (value.toString().length() == 0)
				return new JLabel();

			if (leaf) {
				
				if(selected) {
					contact.setBackground(UIManager.getColor("Tree.selectionBackground"));
					contact.setOpaque(true);
				}

				ImageIcon displayPictureIcon = new ImageIcon(model.getPath() + "icon1.jpg", "Icon");
				JLabel displayPicture = new JLabel(displayPictureIcon);
				displayPicture.setPreferredSize(new Dimension(28, 28));

				ImageIcon statusIcon = new ImageIcon(model.getPath() + "online.png", "Icon");
				JLabel statusIconLabel = new JLabel(statusIcon);
				statusIconLabel.setPreferredSize(new Dimension(16, 16));
				
				JPanel userInfo = new JPanel(new GridLayout(2, 1));
				userInfo.setOpaque(false);
				JLabel username = new JLabel("<html>" + value.toString() + "</html>");
				JLabel personalMessage = new JLabel("<html><i><small>Their personal message.</small></i></html>");
				userInfo.add(username);
				userInfo.add(personalMessage);

				contact.add(Box.createVerticalStrut(2), BorderLayout.NORTH);
				contact.add(displayPicture, BorderLayout.WEST);
				contact.add(userInfo, BorderLayout.CENTER);
				contact.add(statusIconLabel, BorderLayout.EAST);
				contact.add(Box.createVerticalStrut(2), BorderLayout.SOUTH);
				
				return contact;

			} else {
				contact.add(new JLabel("<html><b>" + value.toString() + "</b><html>"));
				return contact;
			}

		}

	}

	// HELPER METHODS
	public Dimension getPreferredSize() {
		return new Dimension(300, 400);
	}

	public void setParent(JFrame frame) {
		parent = (MainWindow) frame;
	}

	public void createNodes(String[] onlineContacts, String[] offlineContacts) {

		contacts.removeAllChildren();

		DefaultMutableTreeNode online = new DefaultMutableTreeNode("Online Contacts");
		DefaultMutableTreeNode offline = new DefaultMutableTreeNode("Offline Contacts");

		// Add online contacts
		for (String str : onlineContacts)
			online.add(new DefaultMutableTreeNode(str));

		// Add offline contacts
		for (String str : offlineContacts)
			offline.add(new DefaultMutableTreeNode(str));

		contacts.add(online);
		contacts.add(offline);

		// Reload the updated model
		((DefaultTreeModel) contactTree.getModel()).reload();

		// Expand all of the tree nodes
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