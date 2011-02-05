package client.ui;

import java.awt.event.*;

import javax.swing.*;

import client.Model;
import client.GimClient;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JMenuItem logout, quit, setOptions, about;
	private JPanel main;
	private static Model model = Model.getInstance();

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
		about = new JMenuItem("About");

		MenuListener menuListener = new MenuListener();
		logout.addActionListener(menuListener);
		quit.addActionListener(menuListener);
		about.addActionListener(menuListener);
		logout.setMnemonic(KeyEvent.VK_L);
		quit.setMnemonic(KeyEvent.VK_Q);
		fileMenu.add(logout);
		fileMenu.add(quit);
		helpMenu.add(about);

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
		// setVisible(true);
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
				model.logout();

				// TODO (heather): should below be moved to ServerConnection,
				// after successful logout?
				// TODO (heather): close all open windows except main window
				
				// gordon: do we want to close all open windows except main window,
				// or just disable the chatbox on them?
				
				LoginPanel panel = new LoginPanel();
				panel.setParent(GimClient.getMainWindow());
				GimClient.getMainWindow().setMainPanel(panel);
				
				// reset the roomids
				GimClient.resetRoomIds();
			} else if (e.getSource().equals(quit)) {
				model.quit();
			} else if (e.getSource().equals(setOptions)) {
				System.out.println("setOptions clicked.");
				// TODO (heather): create options panel

				/*
				OptionsPanel options = new OptionsPanel();

				Object[] buttons = { "Confirm", "Cancel" };
				int n = JOptionPane.showOptionDialog(null, options, "Select options", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);

				
				if (n == 0) { // "confirm"
					if (!options.getNicknameText().equals(model.getSelf().getNickname())) {
						model.setOwnNickname(options.getNicknameText());
						model.setNickname(options.getNicknameText());
						// for when user is not their own friend...
						model.getNickName(model.getOwnUserName());
					}

					if (!options.getStatusText().equals(model.getOwnStatus())) {
						model.setOwnStatus(options.getStatusText());
						model.setStatus(options.getStatusText());
						// for when user is not their own friend...
						model.getStatus(model.getOwnStatus());
					}

					if (!options.getPersonalMessageText().equals(model.getOwnPersonalMessage())) {
						model.setOwnPersonalMessage(options.getPersonalMessageText());
						model.setPersonalMessage(options.getPersonalMessageText());
						// for when user is not their own friend...
						model.getStatus(model.getOwnPersonalMessage());
					}
				}
				*/
				
			}
			else if (e.getSource().equals(about)){
				JOptionPane.showMessageDialog(null, "                Team G Instant Messenger\n 2010-2011\n Developed by: \n James McMinn\n Gordon Martin\n Heather Hoaglund-Biron\n Ewan Baird\n\n Artwork provided by Matt Roszak", 
						"About", JOptionPane.PLAIN_MESSAGE);
			}
		}
		
	}

	class windowListener implements WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {
/*			if (main instanceof ChatPanel) {
				((ChatPanel) main).setIsFocused(true);
			}*/
		}

		@Override
		public void windowClosed(WindowEvent e) {

			// if the window closed is a chat window
			if (main instanceof ChatPanel) {

				String roomid = ((ChatPanel) main).getID();

				// tell the server to leave the room
				model.leave(roomid);

				// remove from the window list
				// GimUI toDispose = GimClient.getWindowRoom(roomid);
				GimClient.removeRoom((ChatPanel) main);
				
				
				//TODO: DISPOSE OF CHAT WINDOWS PROPERLY WHEN CLOSED (below line causes null pointer :\)
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
/*			if (main instanceof ChatPanel) {
				((ChatPanel) main).setIsFocused(false);
			}*/

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
