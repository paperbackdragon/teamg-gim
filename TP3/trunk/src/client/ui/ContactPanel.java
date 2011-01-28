package client.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import client.Model;
import client.GimClient;
import client.User;
import client.UserChangedListener;

public class ContactPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton add, del, chat, group;
	private JList contactList;
	protected JScrollPane scrollPane;
	private Model model = Model.getInstance();
	private PersonalInfo info;
	private User self = model.getSelf();

	/**
	 * Default constructor
	 */
	public ContactPanel() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

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

		private EditableJLabel name, message;
		private JLabel status;

		class TextField extends JPanel {

			private static final long serialVersionUID = 1L;

			public TextField() {

				setLayout(new GridLayout(3, 1, 0, 0));

				name = new EditableJLabel(self.getNickname());
				message = new EditableJLabel(self.getPersonalMessage());

				// Create a listener for the editableJLabels
				ValueChangedListener valueListener = new ValueChangedListener() {
					@Override
					public void valueChanged(String value, JComponent source) {
						if (source.equals(name)) {
							self.setNickname(value);
							model.getOutLink().setNickname(value);
						} else if (source.equals(message)) {
							self.setPersonalMessage(value);
							model.getOutLink().setPersonalMessage(value);
						}
					}
				};

				UserChangedListener selfListener = new UserChangedListener() {
					@Override
					public void DisplayPicChnaged() {
					}

					@Override
					public void nicknameChanged() {
						name.setText(self.getNickname());
					}

					@Override
					public void personalMessageChanged() {
						message.setText(self.getPersonalMessage());
					}

					@Override
					public void statusChanged() {
					}

					@Override
					public void changed() {
					}
				};

				name.addValueChangedListener(valueListener);
				message.addValueChangedListener(valueListener);
				self.addUserChangedListener(selfListener);

				status = new JLabel("<html><font size=\"3\">Status: Online</font></html>");

				add(name);
				add(message);
				add(status);
			}
		}

		public PersonalInfo() {
			setLayout(new BorderLayout(5, 5));
			ImageIcon icon = new ImageIcon(model.getPath() + "icon1.jpg", "Icon");
			JLabel iconLabel = new JLabel(icon);
			iconLabel.setPreferredSize(new Dimension(64, 64));
			add(iconLabel, BorderLayout.WEST);
			add(new TextField(), BorderLayout.CENTER);
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

			this.setLayout(new BorderLayout());

			DefaultListModel listModel = new DefaultListModel();
			final JList list = new JList(listModel);
			contactList = list;
			
			UserChangedListener listener = new UserChangedListener() {
				@Override
				public void statusChanged() {
				}

				@Override
				public void personalMessageChanged() {
				}

				@Override
				public void nicknameChanged() {
				}

				@Override
				public void DisplayPicChnaged() {
				}

				@Override
				public void changed() {
					list.repaint();
				}
			};

			// Create and populate the list model.
			for (User u : model.getFriendList().getFriendList()) {
				listModel.addElement(u);
				u.addUserChangedListener(listener);
			}

			list.addMouseListener(new SingleChatListener());

			list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			ContactListRenderer renderer = new ContactListRenderer();
			list.setCellRenderer(renderer);
			list.setSelectedIndex(0);

			add(list, BorderLayout.NORTH);
		}
	}

	class ContactListRenderer extends JLabel implements ListCellRenderer {

		private static final long serialVersionUID = 1L;

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			User user = (User) value;

			JPanel contact = new JPanel(new BorderLayout(5, 0));
			contact.setOpaque(false);

			ImageIcon displayPictureIcon = new ImageIcon(model.getPath() + "icon1.jpg", "Icon");
			JLabel displayPicture = new JLabel(displayPictureIcon);
			displayPicture.setPreferredSize(new Dimension(32, 32));
			displayPicture.setIconTextGap(0);

			ImageIcon statusIcon;
			if(user.getStatus().equalsIgnoreCase("offline"))
				statusIcon = new ImageIcon(model.getPath() + "offline.png", "Icon");
			else
				statusIcon = new ImageIcon(model.getPath() + "online.png", "Icon");
			
			JLabel statusIconLabel = new JLabel(statusIcon);
			statusIconLabel.setPreferredSize(new Dimension(16, 16));

			JPanel userInfo = new JPanel(new GridLayout(2, 1));
			userInfo.setOpaque(false);
			JLabel username = new JLabel("<html>" + user.getNickname() + "</html>");
			JLabel personalMessage = new JLabel("<html><small>" + user.getPersonalMessage() + "</small></html>");
			personalMessage.setForeground(UIManager.getColor("Tree.textForeground").brighter().brighter());
			userInfo.add(username);
			userInfo.add(personalMessage);

			if (isSelected) {
				contact.setBackground(UIManager.getColor("Tree.selectionBackground"));
				contact.setOpaque(true);
			} else {
				// Set colours to non selected
			}

			contact.add(Box.createVerticalStrut(1), BorderLayout.NORTH);
			contact.add(Box.createHorizontalStrut(2), BorderLayout.WEST);
			contact.add(displayPicture, BorderLayout.WEST);
			contact.add(userInfo, BorderLayout.CENTER);
			contact.add(statusIconLabel, BorderLayout.EAST);
			contact.add(Box.createVerticalStrut(1), BorderLayout.SOUTH);

			return contact;

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

	// HELPER METHODS
	public Dimension getPreferredSize() {
		return new Dimension(300, 400);
	}

	private String[] getSelectedContacts() {
		String[] contact = new String[contactList.getSelectedValues().length];
		int i = 0;
		for(Object u : contactList.getSelectedValues() ) {
			contact[i++] = (String) ((User) u).getEmail();
		}
		return contact;
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
							model.addfriend(s);
						}

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
							model.getFriendList();

						}
					}
				});

			}
			// TODO (heather): make chat grayed out till someone is clicked
			else if (e.getSource().equals(chat)) {
				// Check if there's already a chat open with this user
				int find = GimClient.findRoom(getSelectedContacts()[0]);
				if (find == -1) {
					model.createRoom(false, getSelectedContacts());
				} else {
					GimClient.getWindow(getSelectedContacts()[0]).setVisible(true);
				}
			} else if (e.getSource().equals(group)) {
				model.createRoom(true, getSelectedContacts());
			}
		}
	}

	class SingleChatListener implements MouseListener {

		public void mousePressed(MouseEvent e) {

			if (e.getClickCount() == 2 ) {
				int find = GimClient.findRoom(getSelectedContacts()[0]);

				if (find == -1)
					model.createRoom(false, getSelectedContacts());
				else
					GimClient.getWindow(getSelectedContacts()[0]).setVisible(true);

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
