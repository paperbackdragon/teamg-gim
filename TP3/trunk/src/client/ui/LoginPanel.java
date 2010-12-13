package client.ui;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel{
	
	//CONSTRUCTOR
	public LoginPanel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			System.exit(0);
		}
		
		setLayout(new BorderLayout());
		add(new CenterPanel(), BorderLayout.SOUTH);
	}
	
	//PANELS
	class CenterPanel extends JPanel {
		public CenterPanel() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			//gridbaglayout instead?
			
			add(new JLabel("Login:"));
			
			JPanel emailPanel = new JPanel();
			emailPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			emailPanel.add(new JLabel("Email: "));
			JTextField email = new JTextField("cyblob@gmail.com");
			email.setPreferredSize(new Dimension(200, 30));
			emailPanel.add(email);
			add(emailPanel);
			
			JPanel pwdPanel = new JPanel();
			pwdPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			pwdPanel.add(new JLabel("Password: "));
			JTextField pwd = new JTextField("password");
			pwd.setPreferredSize(new Dimension(200, 30));
			pwdPanel.add(pwd);
			add(pwdPanel);
		}
	}
	
	//HELPER METHODS
	public Dimension getPreferredSize() {
		return new Dimension(300, 400);
	}
}
