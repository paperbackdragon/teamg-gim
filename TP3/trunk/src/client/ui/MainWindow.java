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
import javax.swing.JSeparator;
import javax.swing.UIManager;

import client.Model;
import client.GimClient;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JMenuItem logout, quit, about, viewOffline, showNotifications;
	private JPanel main;
	private static Model model = Model.getInstance();

	// CONSTRUCTOR
	public MainWindow(String title, JPanel panel) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		// Set the window icon
		this.setIconImage(new ImageIcon(model.getPath() + "icons/logo.png").getImage());

		this.setPreferredSize(new Dimension(model.getOptions().mainWindowWidth, model.getOptions().mainWindowHeight));

		// Listen for window resize and update the options
		Listener l = new Listener();
		this.addComponentListener(l);

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
		about = new JMenuItem("About GIM...");

		logout.addActionListener(l);
		quit.addActionListener(l);
		about.addActionListener(l);

		logout.setMnemonic(KeyEvent.VK_L);
		quit.setMnemonic(KeyEvent.VK_Q);

		fileMenu.add(logout);
		add(new JSeparator());
		fileMenu.add(quit);
		helpMenu.add(about);

		this.addWindowListener(l);

		viewOffline = new JCheckBoxMenuItem("Show Offline Users");
		viewOffline.setSelected(model.getOptions().showOffline);
		viewOffline.addActionListener(l);
		optionMenu.add(viewOffline);

		showNotifications = new JCheckBoxMenuItem("Show Notifications");
		showNotifications.setSelected(model.getOptions().showNotifications);
		showNotifications.addActionListener(l);
		optionMenu.add(showNotifications);

		menu.add(fileMenu);
		menu.add(optionMenu);
		menu.add(helpMenu);

		setJMenuBar(menu);
		setContentPane(main);
		pack();
		setVisible(true);
	}

	// HELPER METHODS
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

	class Listener implements WindowListener, ActionListener, ComponentListener {

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

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(logout)) {
				model.getServer().logout();

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

			} else if (e.getSource().equals(showNotifications)) {
				model.getOptions().showNotifications = showNotifications.isSelected();

			} else if (e.getSource().equals(about)) {
				JOptionPane.showMessageDialog(null, "<html><center><h2>Team G Instant Messenger</h2>"
						+ "&copy; 2010-2011</center>" + "<h3>Developed by</h3>" + "<ul>" + "<li>James McMinn"
						+ "<li>Gordon Martin" + "<li>Heather Hoaglund-Biron" + "<li>Ewan Baird" + "</ul>"
						+ "Artwork provided by Matt Roszak", "About", JOptionPane.PLAIN_MESSAGE);
			}
		}

		@Override
		public void windowActivated(WindowEvent arg0) {
			/*
			 * if (main instanceof ChatPanel) { ((ChatPanel)
			 * main).setIsFocused(true); }
			 */
		}

		@Override
		public void windowClosed(WindowEvent e) {
			GimClient.getMainWindow().setVisible(false);
		}

		@Override
		public void windowClosing(WindowEvent e) {
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
		}

		@Override
		public void windowIconified(WindowEvent e) {
		}

		@Override
		public void windowOpened(WindowEvent e) {
		}

	}
}
