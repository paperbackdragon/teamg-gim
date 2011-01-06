package client.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import client.GimClient;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel {

	private MainWindow parent;
	private JTextField email;
	private JPasswordField pwd;
	private JButton loginButton, register;
	private JCheckBox autoLogin, savePassword, rememberUser;

	// CONSTRUCTOR
	public LoginPanel() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		EnterListener enterlistener = new EnterListener();
		LoginListener loginListener = new LoginListener();

		JPanel frame = new JPanel(new BorderLayout());
		frame.setPreferredSize(new Dimension(265, 575));

		// Main JPanel
		JPanel loginFrame = new JPanel();
		loginFrame.setLayout(new BoxLayout(loginFrame, BoxLayout.Y_AXIS));

		// Panel containing email address label and text box
		JPanel emailPanel = new JPanel();
		emailPanel.setLayout(new GridLayout(2, 1, 15, 0));
		email = new JTextField();
		email.addKeyListener(enterlistener);
		emailPanel.add(new JLabel("E-Mail Address"));
		emailPanel.add(email);

		// Panel containing password label and text box
		JPanel pwdPanel = new JPanel();
		pwdPanel.setLayout(new GridLayout(2, 1, 15, 0));
		pwd = new JPasswordField();
		pwd.addKeyListener(enterlistener);
		pwdPanel.add(new JLabel("Password"));
		pwdPanel.add(pwd);

		// Panel containing account options
		JPanel options = new JPanel();
		options.setLayout(new GridLayout(3, 1));
		autoLogin = new JCheckBox("Log me in automatically");
		savePassword = new JCheckBox("Save my password");
		rememberUser = new JCheckBox("Remember me");
		options.add(autoLogin);
		options.add(savePassword);
		options.add(rememberUser);

		// Login button and panel
		JPanel l = new JPanel();
		l.setLayout(new BoxLayout(l, BoxLayout.X_AXIS));
		loginButton = new JButton("Log In");
		loginButton.addActionListener(loginListener);
		l.add(loginButton, BorderLayout.CENTER);

		// Register button
		JPanel registerPanel = new JPanel();
		JLabel notReg = new JLabel("<html><b>Need an account?</b></html>");
		notReg.setAlignmentX(Component.CENTER_ALIGNMENT);
		register = new JButton("Register");
		register.addActionListener(loginListener);
		register.setAlignmentX(Component.CENTER_ALIGNMENT);
		registerPanel.add(notReg);
		registerPanel.add(register);

		// A panel with a border
		JPanel borderedArea = new JPanel();
		borderedArea.setLayout(new BoxLayout(borderedArea, BoxLayout.Y_AXIS));
		TitledBorder title = BorderFactory.createTitledBorder("Please Login");
		borderedArea.setBorder(title);

		// Add things to the bordered panel
		borderedArea.add(emailPanel);
		borderedArea.add(Box.createVerticalStrut(5));
		borderedArea.add(pwdPanel);
		borderedArea.add(Box.createVerticalStrut(10));
		borderedArea.add(options);
		borderedArea.add(Box.createVerticalStrut(10));

		// Add the bordered panel to the window
		loginFrame.add(Box.createVerticalStrut(10));
		loginFrame.add(borderedArea);
		frame.add(Box.createVerticalStrut(5));
		loginFrame.add(l);

		setLayout(new BorderLayout());

		frame.add(loginFrame, BorderLayout.NORTH);
		frame.add(registerPanel, BorderLayout.SOUTH);

		add(Box.createHorizontalStrut(5), BorderLayout.EAST);
		add(frame, BorderLayout.CENTER);
		add(Box.createHorizontalStrut(5), BorderLayout.WEST);
	}

	public void setParent(JFrame frame) {
		parent = (MainWindow) frame;
		parent.canLogout(false);
	}

	private void login() {
		GimClient.getClient().authenticate(email.getText(), pwd.getPassword());
		GimClient.getClient().setOwnUserName(email.getText());
	}

	// ACTION LISTENERS
	class LoginListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(loginButton))
				login();
			else if (e.getSource().equals(register)) {
				RegisterPanel panel = new RegisterPanel();
				panel.setParent(parent);
				parent.setMainPanel(panel);
			}
		}
	}

	class EnterListener implements KeyListener {
		public void keyTyped(KeyEvent e) {
			if (e.getKeyChar() == KeyEvent.VK_ENTER)
				login();
		}

		public void keyPressed(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
		}
	}
	
}
