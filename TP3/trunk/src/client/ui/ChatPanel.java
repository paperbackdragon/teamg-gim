package client.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class ChatPanel extends JPanel{
	private JTextArea messages, chatBox;
	private JButton send;
	
	//CONSTRUCTOR
	public ChatPanel() {
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
		
		//BOTTOM PANEL
		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout());
		chatBox = new JTextArea();
		chatBox.setEditable(true);
		chatBox.setLineWrap(true);
		chatBox.setWrapStyleWord(true);
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
	
	//ACTION LISTENERS
	class SendListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			//if beginning of box
			if (messages.getText().equals("")) {
				messages.append("me: " + chatBox.getText());
			}
			else {
				messages.append("\n" + "me: " + chatBox.getText());
			}
			chatBox.setText("");
		}
	}
}
