package client.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import client.GimClient;
import client.ui.LoginPanel.LoginListener;



public class FindPopup extends JPanel {
	
	JTextField email, username ;
	JButton find;
	private MainWindow parent;

	public FindPopup(){
	FindListener findListener = new FindListener();
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	
	add(Box.createVerticalStrut(10));
	JLabel header = new JLabel("<html><b>Find Contact</b></html>");
	header.setLayout(new FlowLayout(FlowLayout.CENTER));
	add(header);
	
	add(Box.createVerticalStrut(10));
	JPanel emailPanel = new JPanel();
	emailPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	emailPanel.setMaximumSize(new Dimension(275, 30));
	email = new JTextField(17);
	email.setMaximumSize(new Dimension(200, 25));
	emailPanel.add(new JLabel("        E-Mail:"));
	emailPanel.add(email);
	add(emailPanel);
	
	JLabel or = new JLabel("<html><b>OR</b></html>");
	or.setLayout(new FlowLayout(FlowLayout.CENTER));
	add(or);
	
	add(Box.createVerticalStrut(5));
	JPanel namePanel = new JPanel();
	namePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	namePanel.setMaximumSize(new Dimension(275, 30));
	username = new JTextField(17);
	username.setMaximumSize(new Dimension(200, 25));
	namePanel.add(new JLabel("User Name:"));
	namePanel.add(username);
	add(namePanel);
	
	add(Box.createVerticalStrut(10));
	find = new JButton("Find");
	find.setMaximumSize(new Dimension(75, 25));
	find.setAlignmentX(Component.CENTER_ALIGNMENT);
	find.addActionListener(findListener);
	add(find);
	setPreferredSize(new Dimension(350, 175));
	}
}

//ACTION LISTENERS
class FindListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Find")) {
			System.out.println("Find clicked");
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					
					// Gordon's code - sorry ewy - just testing something :P
					GimClient.getClient().addfriend("cyblob@gmail.com");
					// </Gordon's code>
					MainWindow ui = new MainWindow("GIM - Invite Sent", new InviteSent());
					ui.setLocationRelativeTo(null);//center new chat window
					ui.setVisible(true);
					}
			});
		}
		
	}
}

