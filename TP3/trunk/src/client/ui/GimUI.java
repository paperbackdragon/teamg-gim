package client.ui;

import java.awt.event.*;

import javax.swing.*;


import client.GimClient;

@SuppressWarnings("serial")
public class GimUI extends JFrame {
	private JMenuItem logout, quit, setOptions;
	private JPanel main;
	
	//CONSTRUCTOR
	public GimUI(String title, JPanel panel) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			System.exit(0);
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
		//setVisible(true);
	}
	
	//HELPER METHODS
	public void setMainPanel(JPanel newPanel) {
		main = newPanel;
		setContentPane(main);
		pack();
	}
	
	public JPanel getMainPanel() {
		return main;
	}
	
	public void canLogout(Boolean bool) {
		logout.setEnabled(bool);
	}
	
	//ACTION LISTENERS
	class MenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(logout)) {
				GimClient.getClient().logout();
				
				//TODO (heather): should below be moved to ServerConnection, after successful logout?
				//TODO (heather): close all open windows except main window
				LoginPanel panel = new LoginPanel();
				panel.setParent(GimClient.getMainWindow());
				GimClient.getMainWindow().setMainPanel(panel);
			}
			else if(e.getSource().equals(quit)) {
				GimClient.getClient().quit();
			}
			else if(e.getSource().equals(setOptions)) {
				System.out.println("setOptions clicked.");
				//TODO (heather): create options panel
			}
		}
	}
	
	class windowListener implements WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {}

		@Override
		public void windowClosed(WindowEvent e) {
			
			// if the window closed is a chat window
			if (main instanceof ChatPanel) {
				
				String roomid = ((ChatPanel) main).getID();
				
				// tell the server to leave the room
				GimClient.getClient().leave(roomid);
				
				// remove from the window list
				GimUI toDispose = GimClient.getWindowRoom(roomid);
				GimClient.removeRoom((ChatPanel) main);
				//toDispose.dispose();
			}
			
			else {
				System.exit(0);
			}

		}

		@Override
		public void windowClosing(WindowEvent arg0) {}

		@Override
		public void windowDeactivated(WindowEvent arg0) {}

		@Override
		public void windowDeiconified(WindowEvent arg0) {}

		@Override
		public void windowIconified(WindowEvent arg0) {}

		@Override
		public void windowOpened(WindowEvent arg0) {}
		
	}
}
