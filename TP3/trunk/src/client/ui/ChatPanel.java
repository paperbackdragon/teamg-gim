package client.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import client.GimClient;

/**
 * General class for a chat panel.
 * 
 * @author Heather
 */
@SuppressWarnings("serial")
public class ChatPanel extends JPanel {
	private LinkedList<String> messageQueue;

	protected String id;
	protected JTextArea chatBox;
	protected JTextPane messages;
	protected JButton send;

	// TODO (heather): make sure window scrolls down when chatting

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

	private Boolean inProgress = false;

	public Boolean getInProgress() {
		return inProgress;
	}

	public void setInProgress(Boolean inProgress) {
		System.out.println("letting client talk!");
		if (inProgress.equals(true)) {
			sendMessageQueue();
		}
		this.inProgress = inProgress;
	}

	/*
	 * Case where the other user has closed their window. Don't want to cocern
	 * the user with having to wait for room creation
	 */
	private void sendMessageQueue() {
		if (!messageQueue.isEmpty()) {
			for (int i = 0; i < messageQueue.size(); i++) {
				GimClient.getClient().message(id, messageQueue.removeLast());
			}
		}
	}

	public void setChatWith(String chatWith) {
		this.chatWith = chatWith;
	}

	public String getChatWith() {
		return chatWith;
	}

	/**
	 * Constructor for a chat box
	 */
	public ChatPanel(String roomID) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			System.exit(0);
		}

		id = roomID;
		messageQueue = new LinkedList<String>();
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
		if (chatBox.getText().length() > 0) {
			receiveMessage("Me", chatBox.getText());

			if (getInProgress()) {
				GimClient.getClient().message(id, chatBox.getText());
			} else {
				messageQueue.push(chatBox.getText());
				if (id.equals("-1"))
					GimClient.getClient().createRoom(false, new String[] { chatWith });
			}
		}
	}

	/**
	 * Sends a received message to the message log
	 */
	public void receiveMessage(final String sender, final String message) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				final StyledDocument doc = messages.getStyledDocument();

				// Load the default style and add it as the "regular" text
				Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
				Style regular = doc.addStyle("regular", def);
				// Create an italic style
				Style italic = doc.addStyle("italic", regular);
				StyleConstants.setItalic(italic, true);

				// Create a bold style
				Style bold = doc.addStyle("bold", regular);
				StyleConstants.setBold(bold, true);

				String from = "";
				if (!sender.equals("Me")) {

					User user = GimClient.getClient().getUser(sender);
					if (user == null) {
						from = sender;
					} else {
						from = user.getNickname();
					}

				} else {
					from = sender;
				}

				try {
					doc.insertString(doc.getLength(), from + "\n", bold);
					doc.insertString(doc.getLength(), message + "\n", regular);
				} catch (BadLocationException e) {
				}

				messages.setCaretPosition(doc.getLength());

				messageCount += 1;
				if (messageCount == 1)
					showChat();

			}
		});

	}

	/* method to display the chat only a message has been received */
	private void showChat() {
		GimClient.getWindowIdentifierFromId(id).getWindow().setVisible(true);
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
				chatBox.setText(text.substring(0, chatBox.getCaretPosition()-1) + text.substring(chatBox.getCaretPosition()));
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
