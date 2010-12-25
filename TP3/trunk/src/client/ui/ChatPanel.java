package client.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import client.GimClient;

/**
 * General class for a chat panel.
 * 
 * @author Heather
 */
@SuppressWarnings("serial")
public class ChatPanel extends JPanel {
	private String id;
	protected JTextArea messages, chatBox;
	protected JButton send;

	/*
	 * Gordon: proposed. On adding message to messages JTextArea if messageCount
	 * == 0, make window visible
	 */
	private int messageCount;
	// </proposed>

	/*
	 * Gordon: proposed. If someone else opened the chat, grab the username from
	 * the invitations list in the model. (see personal(final String roomid) in
	 * ServerConnection
	 */
	private String chatWith;

	// </proposed>
	
	private Boolean inProgress;

	public Boolean getInProgress() {
		return inProgress;
	}

	public void setInProgress(Boolean inProgress) {
		this.inProgress = inProgress;
	}

	public void setChatWith(String chatWith) {
		this.chatWith = chatWith;

		// set the window title... (dunno how) </Gordon>
	}

	public String getChatWith() {
		return chatWith;
	}

	public void showChat(String id) {
		
		
	}

	/**
	 * Constructor for a chatbox
	 */
	public ChatPanel(String roomID) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			System.exit(0);
		}

		id = roomID;
	}

	// HELPER METHODS
	public Dimension getPreferredSize() {
		return new Dimension(300, 400);
	}

	public String getID() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Method to set the focus of the mouse to the input text box
	 */
	public void setFocus() {
		chatBox.requestFocusInWindow();
	}

	/**
	 * Sends a message to the message log
	 */
	private void sendMessage() {
		// if beginning of box
		if (!chatBox.getText().equals("")) {
			if (messages.getText().equals("")) {
				messages.append("me: " + chatBox.getText());
				GimClient.getClient().message(id, chatBox.getText());
				messageCount += 1;
			} else {
				messages.append("\n" + "me: " + chatBox.getText());
				GimClient.getClient().message(id, chatBox.getText());
			}
		}
		
	}

	/** Sends a received message to the message log */
	public void receiveMessage(String sender, String message) {
		System.out.println("displaying received message");

		// if beginning of box

		if (messages.getText().equals("")) {
			messages.append(sender + ": " + message);
			messageCount += 1;
		} else {
			messages.append("\n" + sender + ": " + message);
		}
		
		
		if (messageCount > 0) {
			showChat(id);
		}
	}

	// ACTION LISTENERS
	public class SendListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			sendMessage();
			chatBox.setText("");
			chatBox.requestFocusInWindow();
		}
		
	}

	/**
	 * A Key listener to send messages upon pressing "Enter"
	 */
	public class EnterListener implements KeyListener {
		public void keyTyped(KeyEvent e) {
			if (e.getKeyChar() == KeyEvent.VK_ENTER) {
				String text = chatBox.getText();
				chatBox.setText(text.substring(0, text.length() - 1));
				sendMessage();
				chatBox.setText("");
			}
		}

		public void keyPressed(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
		}
	}

}
