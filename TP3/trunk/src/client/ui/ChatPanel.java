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
	
	private Timer timer=null;

	protected String id;
	protected JTextArea chatBox;
	protected JTextPane messages;
	protected JButton send;
	private StyledDocument doc;
	private Style regular, bold, italic, self;

	private Smiley[] smilies = { new Smiley(":)", "smiles/Happy_smiley.png"),
			new Smiley(":-)", "smiles/Happy_smiley.png"), new Smiley(":(", "smiles/Sad_smiley.png"),
			new Smiley(":-(", "smiles/Sad_smiley.png"), new Smiley(":P", "smiles/Tonque_out_smiley.png"),
			new Smiley(":-P", "smiles/Tonque_out_smiley.png"), new Smiley(";(", "smiles/Crying_smiley.png"),
			new Smiley(";-(", "smiles/Crying_smiley.png"), new Smiley("(@)", "smiles/Cat.png") };

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
		}

		id = roomID;
		messageQueue = new LinkedList<String>();

		messages = new JTextPane();
		messages.setEditable(false);

		doc = messages.getStyledDocument();

		// Load the default style and add it as the "regular" text
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setAlignment(def, StyleConstants.ALIGN_LEFT);
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
		
		doc.setParagraphAttributes(0, 0, regular, true);

		for (Smiley s : smilies) {
			Style style = doc.addStyle(s.getText(), null);
			StyleConstants.setIcon(style, new ImageIcon(s.getIcon()));
			s.setStyle(style);
		}
		
		
		
		//timer = new Timer(500,new FlashwindowListener(GimClient.getWindowIdentifierFromId(id).getWindow()));
		isFocused = false;
		
	}
	
	public void setIsFocused(Boolean focused) {
		this.isFocused = focused;
	}
	
	public Boolean getIsFocused(){
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

				String msg = message;
				String from = "";
				Style nameStyle = bold;

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
					doc.insertString(doc.getLength(), from + ": ", nameStyle);

					int position = 0;
					int tmp;
					Smiley smiley = null;

					// Parse the message...
					while (position != -1) {
						position = -1;

						for (Smiley s : smilies) {
							tmp = msg.toUpperCase().indexOf(s.getText());
							if (tmp > -1 && (position == -1 || tmp < position)) {
								System.out.println("Found " + s.getText());
								position = tmp;
								smiley = s;
							}
						}

						if (position >= 0 && smiley != null) {
							doc.insertString(doc.getLength(), msg.substring(0, position), regular);
							doc.insertString(doc.getLength(), msg.substring(position, position
									+ smiley.getText().length()), smiley.getStyle());
							msg = msg.substring(position + smiley.getText().length());
						}

					}

					doc.insertString(doc.getLength(), msg + "\n", regular);
					
					
					if (isFocused == false) {
						//timer.start();
					}

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
	
	class FlashwindowListener implements ActionListener
	{
		private Window chatwindow;
		private final native void flashWindow(Window chatwindow);

		public FlashwindowListener(Window window)
		{
			this.chatwindow = window;
		}

		public void actionPerformed(ActionEvent ae)
		{
			flashWindow(chatwindow);
		}
	}

}
