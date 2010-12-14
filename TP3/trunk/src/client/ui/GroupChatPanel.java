package client.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import client.ui.ChatPanel.ContactInfo;
import client.ui.ChatPanel.EnterListener;
import client.ui.ChatPanel.SendListener;
import client.ui.ChatPanel.ContactInfo.TextField;

@SuppressWarnings("unused")
public class GroupChatPanel extends JPanel {
	private JTextArea messages, chatBox, guests;
	private JButton invite, send;
	
	//CONSTRUCTOR
	public GroupChatPanel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			System.exit(0);
		}
		
		setLayout(new BorderLayout());
		messages = new JTextArea();
		messages.setEditable(false);
		messages.setLineWrap(true);
		messages.setWrapStyleWord(true);
		JScrollPane messagePane = new JScrollPane(messages);
		
		guests = new JTextArea("Contacts");
		guests.setEditable(false);
		guests.setLineWrap(true);
		guests.setWrapStyleWord(true);
		JScrollPane guestPane = new JScrollPane(guests);
		
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
		invite = new JButton("Invite");
		invite.setPreferredSize(new Dimension(65,50));
		send.setPreferredSize(new Dimension(65,50));
		SendListener sendListener = new SendListener(); 
		send.addActionListener(sendListener);
		
		chatPanel.setPreferredSize(new Dimension(0, 50));
		chatPanel.add(chatPane, BorderLayout.WEST);
		chatPanel.add(invite);
		chatPanel.add(send, BorderLayout.EAST);
		//END BOTTOM PANEL
		
		add(new ContactManage(), BorderLayout.NORTH);
		add(guestPane, BorderLayout.EAST);
		add(messagePane, BorderLayout.CENTER);
		add(chatPanel, BorderLayout.SOUTH);
		
		//DOESNT WORK:		chatBox.requestFocusInWindow();
	}
	
	//PANELS
	class ContactManage extends JPanel {
		class TextField extends JPanel {
			public JButton addContact = new JButton("Invite Contact");
			
			public TextField() {
				setLayout(new GridLayout(2,2));
				JLabel name = new JLabel("<html><b>Room Name</b></html>");
				JLabel message = new JLabel("Room topic");
				//add(name);
				//add(addContact);
				//add(message);
			}
		}
		
		public ContactManage() {
			add(new TextField());
		}
		
	}
	//HELPER METHODS
	public Dimension getPreferredSize() {
		return new Dimension(500, 500);
	}
	
	public void setFocus() {
		chatBox.requestFocusInWindow();
	}
	
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
	
