package client.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLEditorKit;

import client.Model;
import client.GimClient;
import client.User;

public class ChatWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JMenuItem invite, viewLog, clearWindow, block, remove, close, showTimestamps, enableLogging;
	private ChatPanel main;
	private static Model model = Model.getInstance();

	// CONSTRUCTOR
	public ChatWindow(String title, ChatPanel panel) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		this.main = panel;

		// Set the window icon depending on the type of chat
		if (panel instanceof SingleChatPanel)
			this.setIconImage(((SingleChatPanel) panel).getUser().getDisplayPic().getImage());
		else
			this.setIconImage(new ImageIcon(model.getPath() + "icons/logo.png").getImage());

		setPreferredSize(new Dimension(model.getOptions().chatWindowWidth, model.getOptions().chatWindowHeight));

		this.setTitle(title);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		Listener l = new Listener();
		JMenuBar menu = new JMenuBar();
		JMenu fileMenu = new JMenu("Conversation");
		JMenu optionMenu = new JMenu("Options");
		fileMenu.setMnemonic(KeyEvent.VK_C);
		optionMenu.setMnemonic(KeyEvent.VK_O);

		if (panel instanceof SingleChatPanel) {
			viewLog = new JMenuItem("View Log...");
			viewLog.addActionListener(l);
			viewLog.setMnemonic(KeyEvent.VK_V);
			fileMenu.add(viewLog);
		} else {
			invite = new JMenuItem("Invite...");
			invite.addActionListener(l);
			invite.setMnemonic(KeyEvent.VK_I);
			fileMenu.add(invite);
		}

		clearWindow = new JMenuItem("Clear Window");
		clearWindow.setMnemonic(KeyEvent.VK_L);
		clearWindow.addActionListener(l);
		fileMenu.add(clearWindow);

		fileMenu.add(new JSeparator());

		if (panel instanceof SingleChatPanel) {
			block = new JMenuItem("Block");
			block.addActionListener(l);
			block.setMnemonic(KeyEvent.VK_B);
			fileMenu.add(block);

			remove = new JMenuItem("Remove");
			remove.addActionListener(l);
			fileMenu.add(remove);

			fileMenu.add(new JSeparator());
		}

		close = new JMenuItem("Close");
		close.setMnemonic(KeyEvent.VK_C);
		close.addActionListener(l);
		fileMenu.add(close);

		enableLogging = new JCheckBoxMenuItem("Enable Logging");
		enableLogging.setMnemonic(KeyEvent.VK_L);
		enableLogging.addActionListener(l);
		enableLogging.setSelected(model.getOptions().enableLogging);
		optionMenu.add(enableLogging);

		showTimestamps = new JCheckBoxMenuItem("Show Timestamps");
		showTimestamps.setMnemonic(KeyEvent.VK_T);
		showTimestamps.addActionListener(l);
		showTimestamps.setSelected(model.getOptions().showTimestamps);
		optionMenu.add(showTimestamps);

		menu.add(fileMenu);
		menu.add(optionMenu);

		setJMenuBar(menu);
		this.addWindowListener(l);
		this.addComponentListener(l);

		setContentPane(main);
		pack();
	}

	class Listener implements WindowListener, ComponentListener, ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == close) {
				ChatWindow.this.dispose();
				model.getServer().leave(main.getID());
				GimClient.removeRoom(main);

			} else if (e.getSource() == viewLog) {
				try {
					java.awt.Desktop.getDesktop().browse(new URI("file:///" + main.logfile));
				} catch (IOException e2) {
				} catch (URISyntaxException e2) {
				}
			} else if (e.getSource() == clearWindow) {
				main.messages.setEditorKit(new HTMLEditorKit());

			} else if (e.getSource() == block) {
				model.getFriendList().addBlockedUser(((SingleChatPanel) main).getUser());
				model.getServer().block(((SingleChatPanel) main).getUser().getEmail());
				main.chatBox.setEditable(false);
				model.getServer().leave(main.getID());
				GimClient.removeRoom(main);

			} else if (e.getSource() == remove) {
				model.getFriendList().removeUser(((SingleChatPanel) main).getUser());
				model.getServer().delete(((SingleChatPanel) main).getUser().getEmail());
				model.getServer().leave(main.getID());
				GimClient.removeRoom(main);

			} else if (e.getSource() == invite) {

				User[] participants = ((GroupChatPanel) main).participants;
				String[] temp = new String[participants.length];

				for (int i = 0; i < participants.length; i++) {
					temp[i] = participants[i].toString();
				}

				SelectContactsPanel inputs = new SelectContactsPanel(model.getFriendList().getOnlineUsers(), temp);
				if (inputs.getBoxes().size() != 0) {
					JOptionPane.showMessageDialog(null, inputs, "Select contacts to invite", JOptionPane.PLAIN_MESSAGE);
					ArrayList<JCheckBox> checkboxes = inputs.getBoxes();
					for (int i = 0; i < checkboxes.size(); i++) {
						if (checkboxes.get(i).isSelected() == true) {
							model.getServer().invite(main.id, checkboxes.get(i).getText());
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "No contacts available");
				}

			} else if (e.getSource() == showTimestamps) {
				model.getOptions().showTimestamps = showTimestamps.isSelected();
				main.showTimestamps = showTimestamps.isSelected();
			} else if (e.getSource() == enableLogging) {
				model.getOptions().enableLogging = enableLogging.isSelected();
				main.enableLogging = enableLogging.isSelected();
			}
		}

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

		@Override
		public void windowActivated(WindowEvent arg0) {
			main.setIsFocused(true);
		}

		@Override
		public void windowClosed(WindowEvent e) {
			model.getServer().leave(main.getID());
			GimClient.removeRoom(main);

			if (ChatWindow.this.main.out != null) {
				try {
					ChatWindow.this.main.out.close();
				} catch (IOException e1) {
				}
			}
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
			main.setIsFocused(false);
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
