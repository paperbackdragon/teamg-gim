package client.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import client.GimClient;

/**
 * Specific class for a chat panel used for a conversation between more than two
 * people.
 * 
 * @author Heather
 */
@SuppressWarnings("serial")
public class GroupChatPanel extends ChatPanel {
	private JTextArea guests;
	private JButton invite;
	private String[] participants;

	// CONSTRUCTOR
	public GroupChatPanel(String roomID) {
		super(roomID);
		
		setLayout(new BorderLayout());

		messages = new JTextPane();
		messages.setEditable(false);
		// messages.setLineWrap(true);
		// messages.setWrapStyleWord(true);
		JScrollPane messagePane = new JScrollPane(messages);

		guests = new JTextArea(" ");
		guests.setEditable(false);
		guests.setLineWrap(true);
		guests.setWrapStyleWord(true);
		JScrollPane guestPane = new JScrollPane(guests);

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
		chatPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatPane.setPreferredSize(new Dimension(235, 50));

		send = new JButton("Send");
		invite = new JButton("Invite");

		InviteListener inviteListener = new InviteListener();
		invite.addActionListener(inviteListener);

		invite.setPreferredSize(new Dimension(65, 50));
		send.setPreferredSize(new Dimension(65, 50));
		SendListener sendListener = new SendListener();
		send.addActionListener(sendListener);

		chatPanel.setPreferredSize(new Dimension(0, 50));
		chatPanel.add(chatPane, BorderLayout.WEST);
		chatPanel.add(invite);
		chatPanel.add(send, BorderLayout.EAST);
		// END BOTTOM PANEL

		add(new ContactManage(), BorderLayout.NORTH);
		add(guestPane, BorderLayout.EAST);
		add(messagePane, BorderLayout.CENTER);
		add(chatPanel, BorderLayout.SOUTH);

		// TODO (heather): DOESNT WORK: chatBox.requestFocusInWindow();
	}

	// PANELS
	class ContactManage extends JPanel {
		class TextField extends JPanel {
			public JButton addContact = new JButton("Invite Contact");

			public TextField() {
				setLayout(new GridLayout(2, 2));
				JLabel name = new JLabel("<html><b>Room Name</b></html>");
				JLabel message = new JLabel("Room topic");
				// add(name);
				// add(addContact);
				// add(message);
			}
		}

		public ContactManage() {
			add(new TextField());
		}
	}

	// update the user list... this method may change. may not use a text box to
	// display users
	// in future :P
	public void updateUserList(String[] participants) {
		this.participants = participants;
		String theString = "";
		for (int i = 0; i < participants.length; i++) {
			theString += participants[i] + "\n";
			System.out.println("adding " + participants[i] + " to guest list");
		}
		guests.setText(theString);

	}

	public class InviteListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String[] temp = {" "};
			if (participants == null) {
				participants = temp;
			}
			
			SelectContactsPanel inputs = new SelectContactsPanel(GimClient
					.getClient().getOnlinefriends(), participants);

			JOptionPane.showMessageDialog(null, inputs, "Select contacts to invite",
					JOptionPane.PLAIN_MESSAGE);
			
			ArrayList<JCheckBox> blah = inputs.getBoxes();
			
			for (int i = 0; i < blah.size(); i ++) {
				if (blah.get(i).isSelected() == true) {
					GimClient.getClient().invite(id, blah.get(i).getText());
				}
			}
			
		}
		
		

	}
}
