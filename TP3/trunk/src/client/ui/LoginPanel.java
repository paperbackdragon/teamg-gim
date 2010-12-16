package client.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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
		
		LoginListener loginListener = new LoginListener();
		//TextListener textListener = new TextListener();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(Box.createVerticalStrut(50));
		JLabel login = new JLabel("LOG IN");
		login.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(login);
		
		JPanel emailPanel = new JPanel();
		emailPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		emailPanel.setMaximumSize(new Dimension(275, 30));
		email = new JTextField();
		email.setPreferredSize(new Dimension(200, 25));
		emailPanel.add(new JLabel("E-Mail:"));
		emailPanel.add(email);
		add(emailPanel);
		
		JPanel pwdPanel = new JPanel();
		pwdPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		pwdPanel.setMaximumSize(new Dimension(275, 30));
		pwd = new JPasswordField();
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
	
	//ACTION LISTENERS
	class LoginListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(loginButton)) {
				System.out.println("Login pressed");
				if(parent != null) {
					ContactPanel panel = new ContactPanel();
					panel.setParent(parent);
					parent.setMainPanel(panel);
				}
				else
					System.out.println("parent is null");
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
