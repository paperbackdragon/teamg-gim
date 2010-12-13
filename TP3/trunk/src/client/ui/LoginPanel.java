package client.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel{
	JTextField email, pwd;
	JButton loginButton, register;
	JCheckBox auto;
	
	//CONSTRUCTOR
	public LoginPanel() {
		/*try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			System.exit(0);
		}*/
		
		LoginListener loginListener = new LoginListener();
		//TextListener textListener = new TextListener();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(Box.createVerticalStrut(50));
		JLabel login = new JLabel("LOG IN");
		login.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(login);
		
		add(Box.createVerticalStrut(10));
		email = new JTextField("E-Mail");
		email.setMaximumSize(new Dimension(200, 25));
		//email.addFocusListener(textListener);
		email.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(email);
		
		pwd = new JTextField("Password");
		pwd.setMaximumSize(new Dimension(200, 25));
		//pwd.addFocusListener(textListener);
		pwd.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(pwd);
		
		add(Box.createVerticalStrut(10));
		loginButton = new JButton("LOG IN");
		loginButton.addActionListener(loginListener);
		loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(loginButton);
		
		add(Box.createVerticalStrut(10));
		auto = new JCheckBox("Log in automatically?");
		auto.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(auto);
		
		add(Box.createVerticalStrut(30));
		JLabel notReg = new JLabel("NOT REGISTERED?");
		notReg.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(notReg);
		
		add(Box.createVerticalStrut(10));
		register = new JButton("Register");
		register.addActionListener(loginListener);
		register.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(register);
	}
	
	//HELPER METHODS
	public Dimension getPreferredSize() {
		return new Dimension(300, 400);
	}
	
	//ACTION LISTENERS
	class LoginListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(loginButton)) {
				System.out.println("Login pressed");
			}
			else if (e.getSource().equals(register)) {
				System.out.println("Register pressed");
			}
		}
	}
	
	//do we want this functionality?
	/*class TextListener implements FocusListener {
		public void focusGained(FocusEvent e) {
			JTextField tf = (JTextField) e.getSource();
			if(tf.equals(email)) {
				if(tf.getText().equals("E-Mail"))
					tf.setText("");
			}
			else if(tf.equals(pwd)) {
				if(tf.getText().equals("Password"))
					tf.setText("");
			}
		}
		public void focusLost(FocusEvent e) {
			JTextField tf = (JTextField) e.getSource();
			if(tf.getText().equals("")) {
				if(tf.equals(email))
					tf.setText("E-Mail");
				else if(tf.equals(pwd))
					tf.setText("Password");
			}
		}
	}*/
}
