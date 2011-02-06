package client.ui;

import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;

import client.Model;
import client.GimClient;

public class ChatWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JMenuItem logout, quit, setOptions;
	private JPanel main;
	private static Model model = Model.getInstance();

	// CONSTRUCTOR
	public ChatWindow(String title, JPanel panel) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		this.main = panel;
		this.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				model.getOptions().chatWindowWidth = e.getComponent().getWidth();
				model.getOptions().chatWindowHeight = e.getComponent().getHeight();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});

		setPreferredSize(new Dimension(model.getOptions().chatWindowWidth, model.getOptions().chatWindowHeight));

		setTitle(title);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
	}

	class MenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

		}
	}

	class windowListener implements WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {
			if (main instanceof ChatPanel)
				((ChatPanel) main).setIsFocused(true);
		}

		@Override
		public void windowClosed(WindowEvent e) {
			String roomid = ((ChatPanel) main).getID();

			// tell the server to leave the room
			model.leave(roomid);

			// remove from the window list
			// GimUI toDispose = GimClient.getWindowRoom(roomid);
			GimClient.removeRoom((ChatPanel) main);

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
