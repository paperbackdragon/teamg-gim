package client.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import client.Model;
import client.User;
import client.UserChangedListener;

/**
 * Specific class for a chat panel used for a conversation between more than two
 * people.
 * 
 * @author Heather
 */
public class GroupChatPanel extends ChatPanel {

	private static final long serialVersionUID = 1L;

	private JButton invite;
	private User[] participants;

	private final DefaultListModel listModel;

	private final JList list;

	private final UserChangedListener listener;
	private static Model model = Model.getInstance();

	/**
	 * Constructor
	 * 
	 * @param roomID
	 *            The ID of the room used for identification
	 */
	public GroupChatPanel(String roomID) {
		super(roomID);

		setLayout(new BorderLayout());
		JScrollPane messagePane = new JScrollPane(messages);

		listModel = new DefaultListModel();
		list = new JList(listModel);

		JPanel guestList = new JPanel(new BorderLayout());
		guestList.add(list, BorderLayout.CENTER);
		guestList.setMaximumSize(new Dimension(150, 50));
		
		JScrollPane guestPane = new JScrollPane(guestList);
		guestPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		// The listener we'll be using to keep track of changes to people in
		// the list
		listener = new UserChangedListener() {
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
		participants = new User[1];
		participants[0] = model.getSelf();

		for (User u : participants) {
			listModel.addElement(u);
			u.addUserChangedListener(listener);
		}

		// Make sure the list is rendered correctly
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		ContactListRenderer renderer = new ContactListRenderer();
		list.setCellRenderer(renderer);
		list.setSelectedIndex(0);
		
		// BOTTOM PANEL
		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout());
		chatBox = new JTextArea();
		chatBox.setEditable(true);
		chatBox.setLineWrap(true);
		chatBox.setWrapStyleWord(true);
		EnterListener enterListener = new EnterListener();
		chatBox.addKeyListener(enterListener);
		JScrollPane chatPane = new JScrollPane(chatBox);
		chatPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatPane.setPreferredSize(new Dimension(0, 50));

		send = new JButton("Send");
		invite = new JButton("Invite");

		InviteListener inviteListener = new InviteListener();
		invite.addActionListener(inviteListener);

		invite.setPreferredSize(new Dimension(65, 50));
		send.setPreferredSize(new Dimension(65, 50));
		SendListener sendListener = new SendListener();
		send.addActionListener(sendListener);

		chatPanel.setPreferredSize(new Dimension(0, 50));
		chatPanel.add(chatPane, BorderLayout.CENTER);

		JPanel buttons = new JPanel(new BorderLayout(0, 0));
		buttons.add(invite, BorderLayout.WEST);
		buttons.add(send, BorderLayout.EAST);
		chatPanel.add(buttons, BorderLayout.EAST);
		// END BOTTOM PANEL

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, messagePane, guestPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);

		// Provide minimum sizes for the two components in the split pane
		guestPane.setPreferredSize(new Dimension(150, 50));
		messagePane.setMinimumSize(new Dimension(250, 50));

		add(splitPane, BorderLayout.CENTER);
		add(chatPanel, BorderLayout.SOUTH);
	}

	// PANELS
	class ContactManage extends JPanel {
		private static final long serialVersionUID = 1L;

		class TextField extends JPanel {
			private static final long serialVersionUID = 1L;
			public JButton addContact = new JButton("Invite Contact");

			public TextField() {
				setLayout(new GridLayout(2, 2));
				// add(name);
				// add(addContact);
				// add(message);
			}
		}

		public ContactManage() {
			add(new TextField());
		}
	}

	public void updateUserList(User[] participants) {
		this.participants = participants;

		for (Object o : listModel.toArray()) {
			((User) o).removeUserChangedListener(listener);
		}

		int[] indices = list.getSelectedIndices();
		listModel.clear();

		// Create and populate the list model.
		for (User u : participants) {
			listModel.addElement(u);
			u.addUserChangedListener(listener);
		}

		list.setSelectedIndices(indices);
	}

	public class InviteListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String[] temp = new String[participants.length];

			if (participants == null) {
				temp = new String[1];
				temp[0] = "{ }";
			} else {

				for (int i = 0; i < participants.length; i++) {
					temp[i] = participants[i].toString();
				}
			}

			SelectContactsPanel inputs = new SelectContactsPanel(model.getFriendList().getOnlineUsers(), temp);

			JOptionPane.showMessageDialog(null, inputs, "Select contacts to invite", JOptionPane.PLAIN_MESSAGE);

			ArrayList<JCheckBox> blah = inputs.getBoxes();

			for (int i = 0; i < blah.size(); i++) {
				if (blah.get(i).isSelected() == true) {

					// NOTE (gordon): at the moment, this will be wrong (it will
					// invite by their nickname.
					// need to work out a way to preserve the e-mail address...
					model.invite(id, blah.get(i).getText());
				}
			}

		}

	}
}
