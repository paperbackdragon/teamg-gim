package client.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import client.Model;
import client.GimClient;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JMenuItem logout, quit, setOptions, about, viewOffline;
	private JPanel main;
	private static Model model = Model.getInstance();

	// CONSTRUCTOR
	public MainWindow(String title, JPanel panel) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		
		this.setIconImage(new ImageIcon(model.getPath() + "icons/logo.png").getImage());
		
		// Listen for window resize and update the options
		this.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				model.getOptions().mainWindowWidth = MainWindow.this.getWidth();
				model.getOptions().mainWindowHeight = MainWindow.this.getHeight();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});

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
		about = new JMenuItem("About GIM");

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

		viewOffline = new JCheckBoxMenuItem("Show Offline Users");
		viewOffline.setSelected(model.getOptions().showOffline);
		viewOffline.addActionListener(menuListener);
		optionMenu.add(viewOffline);

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
	
	public Dimension getPreferredSize() {
		return new Dimension(model.getOptions().mainWindowWidth, model.getOptions().mainWindowHeight);
	}

	// ACTION LISTENERS
	class MenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(logout)) {
				model.logout();

				LoginPanel panel = new LoginPanel();
				panel.setParent(GimClient.getMainWindow());
				GimClient.getMainWindow().setMainPanel(panel);

				// reset the roomids
				GimClient.resetRoomIds();

				// disable chat boxes
				GimClient.setChatBoxes(false);

				// clear group chat user lists

				GimClient.clearGroupChats();

			} else if (e.getSource().equals(quit)) {
				model.quit();
			} else if (e.getSource().equals(viewOffline)) {
				model.getOptions().showOffline = viewOffline.isSelected();
				model.getFriendList().forceUpdate();
			} else if (e.getSource().equals(setOptions)) {
				System.out.println("setOptions clicked.");
				// TODO (heather): create options panel

				/*
				 * OptionsPanel options = new OptionsPanel();
				 * 
				 * Object[] buttons = { "Confirm", "Cancel" }; int n =
				 * JOptionPane.showOptionDialog(null, options, "Select options",
				 * JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				 * null, buttons, buttons[0]);
				 * 
				 * 
				 * if (n == 0) { // "confirm" if
				 * (!options.getNicknameText().equals
				 * (model.getSelf().getNickname())) {
				 * model.setOwnNickname(options.getNicknameText());
				 * model.setNickname(options.getNicknameText()); // for when
				 * user is not their own friend...
				 * model.getNickName(model.getOwnUserName()); }
				 * 
				 * if (!options.getStatusText().equals(model.getOwnStatus())) {
				 * model.setOwnStatus(options.getStatusText());
				 * model.setStatus(options.getStatusText()); // for when user is
				 * not their own friend...
				 * model.getStatus(model.getOwnStatus()); }
				 * 
				 * if(!options.getPersonalMessageText().equals(model.
				 * getOwnPersonalMessage())) {
				 * model.setOwnPersonalMessage(options
				 * .getPersonalMessageText());
				 * model.setPersonalMessage(options.getPersonalMessageText());
				 * // for when user is not their own friend...
				 * model.getStatus(model.getOwnPersonalMessage()); } }
				 */

			} else if (e.getSource().equals(about)) {
				JOptionPane
						.showMessageDialog(
								null,
								"<html><center><h2>Team G Instant Messenger</h2>" +
								"&copy; 2010-2011</center>" +
								"<h3>Developed by</h3>" +
								"<ul>" +
								"<li>James McMinn" +
								"<li>Gordon Martin" +
								"<li>Heather Hoaglund-Biron" +
								"<li>Ewan Baird" +
								"</ul>" +
								"<h4>Artwork provided by Matt Roszak</h4>",
								"About", JOptionPane.PLAIN_MESSAGE);
			}
		}

	}

	class windowListener implements WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {
			/*
			 * if (main instanceof ChatPanel) { ((ChatPanel)
			 * main).setIsFocused(true); }
			 */
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
			}

			else {
				GimClient.getMainWindow().setVisible(false);
			}

		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub

		}

	}
}
