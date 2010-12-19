package client.ui;

import java.util.Arrays;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import client.GimClient;
import client.ui.LoginPanel.EnterListener;

@SuppressWarnings("serial")
public class RegisterPanel extends JPanel {
	private GimUI parent;
	private JTextField email;
	private JPasswordField pwd, confirm;
	private JButton register, cancel;
	
	//CONSTRUCTOR
	public RegisterPanel() {
		// TODO uncomment eventually?
		/*try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			System.exit(0);
		}*/
		
		EnterListener enterlistener = new EnterListener();
		RegisterListener regListener = new RegisterListener();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(Box.createVerticalStrut(50));
		JLabel regLabel = new JLabel("REGISTER");
		regLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(regLabel);
		
		add(Box.createVerticalStrut(10));
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
		
		JPanel confirmPanel = new JPanel();
		confirmPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		confirmPanel.setMaximumSize(new Dimension(290, 30));
		confirm = new JPasswordField();
		confirm.addKeyListener(enterlistener);
		confirm.setPreferredSize(new Dimension(200, 25));
		confirmPanel.add(new JLabel ("Confirm: "));
		confirmPanel.add(confirm);
		add(confirmPanel);
		
		JPanel buttonPanel = new JPanel();
		register = new JButton("OK");
		register.addActionListener(regListener);
		cancel = new JButton("Cancel");
		cancel.addActionListener(regListener);
		buttonPanel.add(register);
		buttonPanel.add(cancel);
		add(buttonPanel);
	}
	
	//HELPER METHODS
	public Dimension getPreferredSize() {
		return new Dimension(300, 400);
	}
	
	public void setParent(JFrame frame) {
		parent = (GimUI) frame;
		parent.canLogout(false);
	}
	
	private void register() {
		if(email.getText().isEmpty()) {
			JOptionPane.showMessageDialog(RegisterPanel.this, "Please enter an email.");
		}
		else if(pwd.getPassword().length == 0 || confirm.getPassword().length == 0) {
			JOptionPane.showMessageDialog(RegisterPanel.this, "Please enter a password in both fields.");
		}
		else if(Arrays.equals(pwd.getPassword(), confirm.getPassword())) {
			GimClient.getClient().register(email.getText(), pwd.getPassword());
		}
		else {
			pwd.setText("");
			confirm.setText("");
			JOptionPane.showMessageDialog(RegisterPanel.this, "Passwords do not match.");
		}
	}
	
	//ACTION LISTENERS
	class RegisterListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(register)) {
				register();
			}
			else if(e.getSource().equals(cancel)) {
				LoginPanel panel = new LoginPanel();
				panel.setParent(parent);
				parent.setMainPanel(panel);
			}
		}
	}
	
	class EnterListener implements KeyListener{
		public void keyTyped(KeyEvent e) {
			if(e.getKeyChar() == KeyEvent.VK_ENTER)
				register();
		}
		public void keyPressed(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {}
	}
}
