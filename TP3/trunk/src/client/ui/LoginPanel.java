package client.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import client.GimClient;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel{
	private GimUI parent;
	private JTextField email;
	private JPasswordField pwd;
	private JButton loginButton, register;
	private JCheckBox auto;
	
	//CONSTRUCTOR
	public LoginPanel() {
		/*try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			System.exit(0);
		}*/
		EnterListener enterlistener = new EnterListener();
		LoginListener loginListener = new LoginListener();
		//TextListener textListener = new TextListener();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(Box.createVerticalStrut(50));
		JLabel login = new JLabel("LOG IN");
		login.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(login);
		
		JPanel emailPanel = new JPanel();
		emailPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		emailPanel.setMaximumSize(new Dimension(290, 30));
		email = new JTextField();
		email.addKeyListener(enterlistener);
		email.setPreferredSize(new Dimension(200, 25));
		emailPanel.add(new JLabel("E-Mail:"));
		emailPanel.add(email);
		add(emailPanel);
		
		JPanel pwdPanel = new JPanel();
		pwdPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		pwdPanel.setMaximumSize(new Dimension(290, 30));
		pwd = new JPasswordField();
		pwd.addKeyListener(enterlistener);
		pwd.setPreferredSize(new Dimension(200, 25));
		pwdPanel.add(new JLabel ("Password:"));
		pwdPanel.add(pwd);
		add(pwdPanel);
		
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
	
	public void setParent(JFrame frame) {
		parent = (GimUI) frame;
	}
	
	private void login() {
		GimClient.getClient().authenticate(email.getText(), pwd.getPassword());
	}
	
	//ACTION LISTENERS
	class LoginListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(loginButton)) {
				System.out.println("Login pressed");
				login();
			}
			else if (e.getSource().equals(register)) {
				System.out.println("Register pressed");
				if(parent != null) {
					RegisterPanel panel = new RegisterPanel();
					panel.setParent(parent);
					parent.setMainPanel(panel);
				}
				else
					System.out.println("parent is null");
			}
		}
	}
	
	class EnterListener implements KeyListener{
		public void keyTyped(KeyEvent e) {
			if(e.getKeyChar() == KeyEvent.VK_ENTER) {
				System.out.println("Login pressed");
				login();
			}
		}
		public void keyPressed(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {}
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
