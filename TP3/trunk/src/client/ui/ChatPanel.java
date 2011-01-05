package client.ui;

import java.awt.*;
import java.awt.event.*;
import java.net.URISyntaxException;
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
 */
@SuppressWarnings("serial")
public class ChatPanel extends JPanel {
	private LinkedList<String> messageQueue;

	private Timer timer = null;

	protected String id;
	protected JTextArea chatBox;
	protected JTextPane messages;
	protected JButton send;
	private StyledDocument doc;
	private Style regular, bold, italic, self;

	private Smiley[] smilies = { new Smiley(":)", "Happy_smiley.png"), new Smiley(":-)", "Happy_smiley.png"),
			new Smiley(":(", "Sad_smiley.png"), new Smiley(":-(", "Sad_smiley.png"),
			new Smiley(":P", "Tonque_out_smiley.png"), new Smiley(":-P", "Tonque_out_smiley.png"),
			new Smiley(";(", "Crying_smiley.png"), new Smiley(";-(", "Crying_smiley.png"),
			new Smiley("(@)", "Cat.png"), new Smiley("CALEF13", "calef13.png") };

	private int messageCount;
	private String chatWith;

	private Boolean inProgress = false;
	private boolean isFocused;

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
	 * Case where the other user has closed their window. Don't want to concern
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
	 * Constructor for a chat box. Creates styles, smiles and everything else we
	 * need
	 */
	public ChatPanel(String roomID) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		id = roomID;
		messageQueue = new LinkedList<String>();

		// Try and get the path to wherever this is running from
		String smileyPath = "";
		try {
			smileyPath = GimClient.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		} catch (URISyntaxException e) {
		}
		smileyPath = smileyPath.substring(0, smileyPath.lastIndexOf("/")) + "/smiles/";

		// Create a new document for the messages
		messages = new JTextPane();
		messages.setEditable(false);
		doc = messages.getStyledDocument();

		// Load the default style and add it as the "regular" text
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setAlignment(def, StyleConstants.ALIGN_LEFT);

		// This apparently sets the font to be the default system font. What the
		// hell TabbedPane.font means I have no idea
		StyleConstants.setFontFamily(def, UIManager.getDefaults().getFont("TabbedPane.font").getFontName());
		StyleConstants.setSpaceAbove(def, 4f);
		regular = doc.addStyle("regular", def);

		// Create an italic style
		italic = doc.addStyle("italic", regular);
		StyleConstants.setItalic(italic, true);

		// Create a bold style
		bold = doc.addStyle("bold", regular);
		StyleConstants.setBold(bold, true);

		// Style for the current users name
		self = doc.addStyle("self", bold);
		StyleConstants.setForeground(self, Color.RED);

		// This line here. Yes, this one. It can die and go to Hell.
		doc.setParagraphAttributes(0, 0, regular, true);

		for (Smiley s : smilies) {
			Style style = doc.addStyle(s.getText(), null);
			StyleConstants.setIcon(style, new ImageIcon(smileyPath + s.getIcon()));
			s.setStyle(style);
		}

		// timer = new Timer(500,new
		// FlashwindowListener(GimClient.getWindowIdentifierFromId(id).getWindow()));
		isFocused = false;

	}

	public void setIsFocused(Boolean focused) {
		this.isFocused = focused;
	}

	public Boolean getIsFocused() {
		return isFocused;
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
	 * Set the focus of the mouse to the input text box
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
	 * 
	 * @param sender
	 *            The id of the person who sent the message
	 * @param message
	 *            The message itself
	 */
	public void receiveMessage(final String sender, final String message) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {

				String msg = message;
				String from = "";
				Style nameStyle = bold;

				// Assign correct styles and names to the sender
				if (!sender.equals("Me")) {
					User user = GimClient.getClient().getUser(sender);
					if (user == null)
						from = sender;
					else
						from = user.getNickname();
					nameStyle = bold;
				} else {
					from = sender;
					nameStyle = self;
				}

				try {
					// Add the name of the sender to the chat
					doc.insertString(doc.getLength(), from + ": ", nameStyle);

					int position = 0;
					int tmp;
					Smiley smiley = null;

					// Parse the message...
					while (position != -1) {
						position = -1;

						// Check for the smiley closest to the start of the text
						for (Smiley s : smilies) {
							tmp = msg.toUpperCase().indexOf(s.getText());
							if (tmp > -1 && (position == -1 || tmp < position)) {
								System.out.println("Found " + s.getText());
								position = tmp;
								smiley = s;
							}
						}

						// Check that we've found a smiley
						if (position >= 0 && smiley != null) {
							// Add the message before the smiley and the smiley
							// itself to the chat
							doc.insertString(doc.getLength(), msg.substring(0, position), regular);
							doc.insertString(doc.getLength(), msg.substring(position, position
									+ smiley.getText().length()), smiley.getStyle());

							// Chop everything before and including the smiley
							// off
							msg = msg.substring(position + smiley.getText().length());
						}

					}

					// Insert any remaining text into the chat
					doc.insertString(doc.getLength(), msg + "\n", regular);

					if (isFocused == false) {
						GimClient.getTrayIcon().displayMessage(from + " says: ", message, TrayIcon.MessageType.INFO);
					}

				} catch (BadLocationException e) {
				}

				// Scroll to the end of the chat
				messages.setCaretPosition(doc.getLength());

				messageCount += 1;
				if (messageCount == 1)
					showChat();

			}
		});

	}

	/**
	 * Show the chat window
	 */
	private void showChat() {
		GimClient.getWindowIdentifierFromId(id).getWindow().setVisible(true);
	}

	/**
	 * Action listeners
	 */
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
				chatBox.setText(text.substring(0, chatBox.getCaretPosition() - 1)
						+ text.substring(chatBox.getCaretPosition()));
				sendMessage();
				chatBox.setText("");
			}
		}

		public void keyPressed(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
		}
	}

	class FlashwindowListener implements ActionListener {
		private Window chatwindow;

		private final native void flashWindow(Window chatwindow);

		public FlashwindowListener(Window window) {
			this.chatwindow = window;
		}

		public void actionPerformed(ActionEvent ae) {
			flashWindow(chatwindow);
		}
	}

}
