package client.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class ChatPanel extends JPanel{
	private JTextArea messages, chatBox;
	private JButton send;
	
	/**
	 * Constructor for a chatbox
	 * 
	 */
	public ChatPanel() {
		/*try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			System.exit(0);
		}*/
		
		setLayout(new BorderLayout());
		
		messages = new JTextArea();
		messages.setEditable(false);
		messages.setLineWrap(true);
		messages.setWrapStyleWord(true);
		JScrollPane messagePane = new JScrollPane(messages);
		
		//BOTTOM PANEL
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
		chatPane.setPreferredSize(new Dimension(235, 50));
		
		send = new JButton("Send");
		send.setPreferredSize(new Dimension(65,50));
		SendListener sendListener = new SendListener(); 
		send.addActionListener(sendListener);
		
		chatPanel.setPreferredSize(new Dimension(0, 50));
		chatPanel.add(chatPane, BorderLayout.WEST);
		chatPanel.add(send, BorderLayout.EAST);
		//END BOTTOM PANEL
		
		add(new ContactInfo(), BorderLayout.NORTH);
		add(messagePane, BorderLayout.CENTER);
		add(chatPanel, BorderLayout.SOUTH);
		
		//DOESNT WORK:		chatBox.requestFocusInWindow();
	}
	
	//PANELS
	class ContactInfo extends JPanel {
		class TextField extends JPanel {
			public TextField() {
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				JLabel name = new JLabel("<html><b>Contact Nickname</b></html>");
				JLabel message = new JLabel("Personal Message");
				JLabel status = new JLabel("<html><font size=\"3\">Status: Online</font></html>");
				add(name);
				add(message);
				add(status);
			}
		}
		/**
		 * A panel containing the information of the contact
		 * 
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
	}
	
	//HELPER METHODS
	public Dimension getPreferredSize() {
		return new Dimension(300, 400);
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
		//if beginning of box
		if (!chatBox.getText().equals("")){
			if (messages.getText().equals("")) {
				messages.append("me: " + chatBox.getText());
			}
			else {
				messages.append("\n" + "me: " + chatBox.getText());
			}
		}
	}
	
	//ACTION LISTENERS
	class SendListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			sendMessage();
			chatBox.setText("");
			chatBox.requestFocusInWindow();
		}
	}
	/**
	 * A Key listener to send messages upon pressing "Enter"
	 */
	class EnterListener implements KeyListener{
		public void keyTyped(KeyEvent e) {
			if(e.getKeyChar() == KeyEvent.VK_ENTER) {
				String text = chatBox.getText();
				chatBox.setText(text.substring(0, text.length()-1));
				sendMessage();
				chatBox.setText("");
			}
		}
		public void keyPressed(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {}
	}
}
