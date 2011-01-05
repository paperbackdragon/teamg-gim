package client.ui;

import java.awt.event.*;

import javax.swing.*;

import client.GimClient;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	private JMenuItem logout, quit, setOptions;
	private JPanel main;

	// CONSTRUCTOR
	public MainWindow(String title, JPanel panel) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		setTitle(title);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setMainPanel(panel);

		JMenuBar menu = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu optionMenu = new JMenu("Options");
		JMenu helpMenu = new JMenu("Help");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		optionMenu.setMnemonic(KeyEvent.VK_O);
		helpMenu.setMnemonic(KeyEvent.VK_H);

		logout = new JMenuItem("Logout");
		quit = new JMenuItem("Quit");

		MenuListener menuListener = new MenuListener();
		logout.addActionListener(menuListener);
		quit.addActionListener(menuListener);
		logout.setMnemonic(KeyEvent.VK_L);
		quit.setMnemonic(KeyEvent.VK_Q);
		fileMenu.add(logout);
		fileMenu.add(quit);

		windowListener windowlistener = new windowListener();
		this.addWindowListener(windowlistener);

		setOptions = new JMenuItem("Set Options...");
		setOptions.addActionListener(menuListener);
		optionMenu.add(setOptions);

		menu.add(fileMenu);
		menu.add(optionMenu);
		menu.add(helpMenu);

		setJMenuBar(menu);
		setContentPane(main);
		pack();
		// setVisible(true);
	}

	// HELPER METHODS
	public void setMainPanel(JPanel newPanel) {
		main = newPanel;
		setContentPane(main);
		pack();
		//setVisible(true);
	}

	public JPanel getMainPanel() {
		return main;
	}

	public void canLogout(Boolean bool) {
		logout.setEnabled(bool);
	}

	// ACTION LISTENERS
	class MenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(logout)) {
				GimClient.getClient().logout();

				// TODO (heather): should below be moved to ServerConnection,
				// after successful logout?
				// TODO (heather): close all open windows except main window
				LoginPanel panel = new LoginPanel();
				panel.setParent(GimClient.getMainWindow());
				GimClient.getMainWindow().setMainPanel(panel);
			} else if (e.getSource().equals(quit)) {
				GimClient.getClient().quit();
			} else if (e.getSource().equals(setOptions)) {
				System.out.println("setOptions clicked.");
				// TODO (heather): create options panel

				OptionsPanel options = new OptionsPanel();

				Object[] buttons = { "Confirm", "Cancel" };
				int n = JOptionPane.showOptionDialog(null, options, "Select options", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);

				if (n == 0) { // "confirm"
					if (!options.getNicknameText().equals(GimClient.getClient().getOwnNickname())) {
						GimClient.getClient().setOwnNickname(options.getNicknameText());
						GimClient.getClient().setNickname(options.getNicknameText());
						// for when user is not their own friend...
						GimClient.getClient().getNickName(GimClient.getClient().getOwnUserName());
					}

					if (!options.getStatusText().equals(GimClient.getClient().getOwnStatus())) {
						GimClient.getClient().setOwnStatus(options.getStatusText());
						GimClient.getClient().setStatus(options.getStatusText());
						// for when user is not their own friend...
						GimClient.getClient().getStatus(GimClient.getClient().getOwnStatus());
					}

					if (!options.getPersonalMessageText().equals(GimClient.getClient().getOwnPersonalMessage())) {
						GimClient.getClient().setOwnPersonalMessage(options.getPersonalMessageText());
						GimClient.getClient().setPersonalMessage(options.getPersonalMessageText());
						// for when user is not their own friend...
						GimClient.getClient().getStatus(GimClient.getClient().getOwnPersonalMessage());
					}
				}
			}
		}
	}

	class windowListener implements WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {
			if (main instanceof ChatPanel) {
				((ChatPanel) main).setIsFocused(true);
			}
		}

		@Override
		public void windowClosed(WindowEvent e) {

			// if the window closed is a chat window
			if (main instanceof ChatPanel) {

				String roomid = ((ChatPanel) main).getID();

				// tell the server to leave the room
				GimClient.getClient().leave(roomid);

				// remove from the window list
				// GimUI toDispose = GimClient.getWindowRoom(roomid);
				GimClient.removeRoom((ChatPanel) main);
				// toDispose.dispose();
			}

			else {
				GimClient.getMainWindow().setVisible(false);
			}

		}

		@Override
		public void windowClosing(WindowEvent arg0) {
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
			if (main instanceof ChatPanel) {
				((ChatPanel) main).setIsFocused(false);
			}
			
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
		}

	}
}
