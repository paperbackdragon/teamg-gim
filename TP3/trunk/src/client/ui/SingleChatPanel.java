package client.ui;

import java.awt.*;
import javax.swing.*;

/**
 * Specific class for a chat panel used for a conversation between two people.
 * 
 * @author Heather
 */
@SuppressWarnings("serial")
public class SingleChatPanel extends ChatPanel {

	private ContactInfo info;

	// CONSTRUCTOR
	public SingleChatPanel(String roomID) {
		super(roomID);
		setLayout(new BorderLayout());


		// messages.setLineWrap(true);
		// messages.setWrapStyleWord(true);
		JScrollPane messagePane = new JScrollPane(messages);

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
		send.setPreferredSize(new Dimension(65, 50));
		SendListener sendListener = new SendListener();
		send.addActionListener(sendListener);

		chatPanel.setPreferredSize(new Dimension(0, 50));
		chatPanel.add(chatPane, BorderLayout.CENTER);
		chatPanel.add(send, BorderLayout.EAST);
		// END BOTTOM PANEL

		this.info = new ContactInfo();
		add(info, BorderLayout.NORTH);
		add(messagePane, BorderLayout.CENTER);
		add(chatPanel, BorderLayout.SOUTH);

		// TODO (heather): DOESNT WORK: chatBox.requestFocusInWindow();

	}

	public void setStatus(String status) {
		System.out
				.println("i'm attempting to call the method to update the status label");
		info.setStatus(status);
	}

	public void setNickname(String name) {
		System.out.println("fuu");
		info.setNickname(name);
	}

	public void setPersonalMessage(String message) {
		info.setMessage(message);
	}

	// PANELS
	class ContactInfo extends JPanel {
		private JLabel name;
		private JLabel message;
		private JLabel status;

		class TextField extends JPanel {
			public TextField() {
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				name = new JLabel("sdsdf... woops. sort.");
				message = new JLabel("Personal Message");
				status = new JLabel(
						"<html><font size=\"3\">Status: Online</font></html>");
				add(name);
				add(message);
				add(status);
			}
		}

		/**
		 * A panel containing the information of the contact
		 */
		public ContactInfo() {
			setLayout(new FlowLayout(FlowLayout.LEFT));
			ImageIcon icon = createImageIcon("icon1.jpg", "Icon");
			JLabel iconLabel = new JLabel(icon);
			iconLabel.setPreferredSize(new Dimension(50, 50));
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
			this.name.setText("<html><b>" + name + "</b></html>");

		}

		public void setStatus(String status) {
			this.status.setText(status);
		}

		public void setMessage(String message) {
			this.message.setText(message);
		}
	}
}
