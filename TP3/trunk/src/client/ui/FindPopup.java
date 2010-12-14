package client.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import client.ui.LoginPanel.LoginListener;

public class FindPopup extends JPanel {
	
	JTextField email, username ;
	JButton find;

	public FindPopup(){
	FindListener findListener = new FindListener();
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	
	add(Box.createVerticalStrut(10));
	JLabel header = new JLabel("<html><b>Find Contact</b></html>");
	header.setAlignmentX(Component.CENTER_ALIGNMENT);
	add(header);
	
	add(Box.createVerticalStrut(10));
	email = new JTextField("E-Mail");
	email.setMaximumSize(new Dimension(200, 25));
	email.setAlignmentX(Component.CENTER_ALIGNMENT);
	add(email);
	
	JLabel or = new JLabel("<html><b>OR</b></html>");
	or.setAlignmentX(Component.CENTER_ALIGNMENT);
	add(or);
	
	username = new JTextField("User Name");
	username.setMaximumSize(new Dimension(200, 25));
	username.setAlignmentX(Component.CENTER_ALIGNMENT);
	add(username);
	
	add(Box.createVerticalStrut(10));
	find = new JButton("Find");
	find.setMaximumSize(new Dimension(75, 25));
	find.setAlignmentX(Component.CENTER_ALIGNMENT);
	find.addActionListener(findListener);
	add(find);
	setPreferredSize(new Dimension(350, 150));
	}
}

//ACTION LISTENERS
class FindListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Find")) {
			System.out.println("Find clicked");
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					GimUI ui = new GimUI("GIM - Invite Sent", new InviteSent());
					ui.setLocationRelativeTo(null);//center new chat window
					}
			});
		}
		
	}
}