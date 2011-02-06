package client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import util.Base64;
import util.Html;

import client.FriendListChangedListener;
import client.Model;
import client.GimClient;
import client.User;
import client.UserChangedListener;
import client.ValueChangedListener;

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

	private class PersonalInfo extends JPanel {

		private static final long serialVersionUID = 1L;

		private EditableJLabel name, message;
		private ListJLabel status;

		class OwnInformation extends JPanel {

			private static final long serialVersionUID = 1L;

			public OwnInformation() {

				setLayout(new GridLayout(3, 1, 0, 0));

				name = new EditableJLabel(Html.escape(self.getNickname()));
				message = new EditableJLabel(Html.escape(self.getPersonalMessage()));
				status = new ListJLabel(new String[] { "Online", "Away", "Busy" }, 0);

				// Create a listener for the editableJLabels
				ValueChangedListener valueListener = new ValueChangedListener() {
					@Override
					public void valueChanged(String value, JComponent source) {
						if (source.equals(name)) {
							self.setNickname(value);
							model.getServer().setNickname(value);
						} else if (source.equals(message)) {
							self.setPersonalMessage(value);
							model.getServer().setPersonalMessage(value);
						} else if (source.equals(status)) {
							model.getServer().setStatus(status.getValue());
						}
					}
				};

				UserChangedListener selfListener = new UserChangedListener() {
					@Override
					public void displayPicChanged() {
					}

					@Override
					public void nicknameChanged() {
						name.setText(Html.escape(self.getNickname()));
					}

					@Override
					public void personalMessageChanged() {
						message.setText(Html.escape(self.getPersonalMessage()));
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
				status.addValueChangedListener(valueListener);

				add(name);
				add(message);
				add(status);
			}
		}

		public PersonalInfo() {

			ChoosePic select = new ChoosePic();
			setLayout(new BorderLayout(5, 0));
			ImageIcon icon = self.getDisplayPic(64, 64);
			final JButton b = new JButton(icon);
			b.addActionListener(select);
			b.setPreferredSize(new Dimension(64, 64));

			self.addUserChangedListener(new UserChangedListener() {
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
				public void displayPicChanged() {
					b.setIcon(self.getDisplayPic(64, 64));
				}

				@Override
				public void changed() {
				}
			});

			add(b, BorderLayout.WEST);
			add(new OwnInformation(), BorderLayout.CENTER);
		}

	}

	// User selects picture, in progess....
	// TODO: Make this do useful stuff
	class ChoosePic implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG file", "jpg", "jpeg");
			JFileChooser chooser = new JFileChooser();
			chooser.addChoosableFileFilter(filter);

			int option = chooser.showOpenDialog(getParent());

			if (option == JFileChooser.APPROVE_OPTION) {
				try {
					File resized = new File(model.getPath() + "/display_picutre.jpg");
					BufferedImage originalImage = ImageIO.read(chooser.getSelectedFile());
					int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

					BufferedImage resizedImage = new BufferedImage(128, 128, type);
					Graphics2D g = resizedImage.createGraphics();
					g.drawImage(originalImage, 0, 0, 128, 128, null);
					g.dispose();

					ImageIO.write(resizedImage, "jpg", resized);

					FileInputStream in = new FileInputStream(resized);
					int c, count = 0;
					byte[] data = new byte[in.available()];

					while ((c = in.read()) != -1) {
						data[count++] = (byte) c;
					}

					String displayPic = Base64.encodeToString(data, false);
					self.setDisplayPic(displayPic);
					model.getServer().setDisplayPicture(displayPic);

				} catch (IOException e1) {
				}

			}
		}
	}

	class ContactList extends JPanel {

		private static final long serialVersionUID = 1L;

		public ContactList() {
			this.setLayout(new BorderLayout());
			this.setBackground(UIManager.getColor("EditorPane.background"));

			// Create a Panel for the header and the online list
			JPanel online = new JPanel(new BorderLayout());
			online.setOpaque(false);

			// Create the list
			final DefaultListModel listModel = new DefaultListModel();
			final JList list = new JList(listModel);
			contactList = list;

			// Create a heading
			final JLabel onlineLabel = new JLabel("<html><b>Online Contacts ("
					+ model.getFriendList().getOnlineUsers().size() + "/"
					+ model.getFriendList().getFriendList().size() + ")</b></html>");
			onlineLabel.setBorder(BorderFactory.createMatteBorder(1, 2, 1, 2, UIManager.getColor("List.background")));
			online.add(onlineLabel, BorderLayout.NORTH);

			// The listener we'll be using to keep track of changes to people in
			// the list
			final UserChangedListener listener = new UserChangedListener() {
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
				public void displayPicChanged() {
				}

				@Override
				public void changed() {
					list.repaint();
				}
			};

			// Create and populate the list model.
			for (User u : model.getFriendList().getOnlineUsers()) {
				listModel.addElement(u);
				u.addUserChangedListener(listener);
			}

			model.getFriendList().addFriendListChangedListener(new FriendListChangedListener() {

				@Override
				public void stateChanged() {
					for (Object o : listModel.toArray()) {
						((User) o).removeUserChangedListener(listener);
					}

					int[] indices = list.getSelectedIndices();
					listModel.clear();

					// Create and populate the list model.
					for (User u : model.getFriendList().getOnlineUsers()) {
						listModel.addElement(u);
						u.addUserChangedListener(listener);
					}

					list.setSelectedIndices(indices);

					onlineLabel.setText("<html><b>Online Contacts (" + model.getFriendList().getOnlineUsers().size()
							+ "/" + model.getFriendList().getFriendList().size() + ")</b></html>");

				}

				@Override
				public void friendRemove(User user) {
				}

				@Override
				public void friendAdded(User user) {
				}
			});

			// Add the listeners
			list.addMouseListener(new SingleChatListener());

			// Make sure the list is rendered correctly
			list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			ContactListRenderer renderer = new ContactListRenderer();
			list.setCellRenderer(renderer);
			list.setSelectedIndex(0);

			online.add(list, BorderLayout.CENTER);
			this.add(online, BorderLayout.NORTH);
		}
	}

	class ButtonPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		public ButtonPanel() {
			setLayout(new GridLayout(1, 4, 5, 5));
			add = new JButton();
			add.setIcon(new ImageIcon(new ImageIcon(model.getPath() + "/icons/add.png").getImage().getScaledInstance(
					32, 20, Image.SCALE_SMOOTH)));

			del = new JButton();
			del.setIcon(new ImageIcon(new ImageIcon(model.getPath() + "/icons/remove.png").getImage()
					.getScaledInstance(32, 20, Image.SCALE_SMOOTH)));

			chat = new JButton();
			chat.setIcon(new ImageIcon(new ImageIcon(model.getPath() + "/icons/chat.png").getImage().getScaledInstance(
					32, 20, Image.SCALE_SMOOTH)));

			group = new JButton();
			group.setIcon(new ImageIcon(new ImageIcon(model.getPath() + "/icons/group.png").getImage()
					.getScaledInstance(32, 20, Image.SCALE_SMOOTH)));

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

	public Dimension getPreferredSize() {
		return new Dimension(300, 500);
	}

	private LinkedList<User> getSelectedContacts() {
		LinkedList<User> users = new LinkedList<User>();

		for (Object u : contactList.getSelectedValues()) {
			users.add((User) u);
		}

		return users;
	}

	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(add)) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {

						String s = (String) JOptionPane.showInputDialog(null,
								"Input the user name of the person you wish to add to your contacts", "Add friend",
								JOptionPane.PLAIN_MESSAGE, null, null, "");

						if (s != null) {
							model.addfriend(s);
						}

					}
				});
			} else if (e.getSource().equals(del) && getSelectedContacts().size() > 0) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {

						LinkedList<User> selectedUsers = getSelectedContacts();
						String users = "";
						for (User u : selectedUsers) {
							users += u.getEmail() + " ";
						}

						Object[] options = { "Yes", "No", };
						int n = JOptionPane.showOptionDialog(null, "Are you sure you want to remove: " + users
								+ " from your contacts?", "remove friends", JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

						if (n == 0) {
							for (User u : selectedUsers) {
								model.getServer().delete(u.getEmail());
								model.getFriendList().removeUser(u);
							}
						}

					}
				});

			} else if (e.getSource().equals(chat) && getSelectedContacts().size() > 0) {

				LinkedList<User> selectedUsers = getSelectedContacts();

				// Check if there's already a chat open with this user
				int find = GimClient.findRoom(selectedUsers.getFirst());

				if (find == -1)
					model.createRoom(getSelectedContacts().getFirst());
				else
					GimClient.getWindow(selectedUsers.getFirst()).setVisible(true);

			} else if (e.getSource().equals(group) && getSelectedContacts().size() > 0) {
				model.createRoom(getSelectedContacts());
			}
		}
	}

	class SingleChatListener implements MouseListener {

		public void mousePressed(MouseEvent e) {

			LinkedList<User> selectedUsers = getSelectedContacts();

			if (e.getClickCount() == 2) {
				int find = GimClient.findRoom(selectedUsers.getFirst());

				if (find == -1)
					model.createRoom(getSelectedContacts().getFirst());
				else
					GimClient.getWindow(selectedUsers.getFirst()).setVisible(true);
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
