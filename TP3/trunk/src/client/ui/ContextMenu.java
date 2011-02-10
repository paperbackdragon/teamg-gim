package client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import client.GimClient;
import client.Model;
import client.User;

public class ContextMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;

	Model model = Model.getInstance();

	JMenuItem viewLogs, startChat, remove, block;
	User user;

	public ContextMenu(User u) {
		this.user = u;

		Listener l = new Listener();

		viewLogs = new JMenuItem("View Chat Logs...");
		viewLogs.addActionListener(l);
		add(viewLogs);
		add(new JSeparator());

		startChat = new JMenuItem("Start Chat...");
		startChat.addActionListener(l);
		
		if (user.getStatus().equalsIgnoreCase("offline") || model.getFriendList().isBlocked(user.getEmail()))
			startChat.setEnabled(false);

		add(startChat);
		add(new JSeparator());

		remove = new JMenuItem("Remove Friend");
		remove.addActionListener(l);
		add(remove);

		if (model.getFriendList().isBlocked(user.getEmail()))
			block = new JMenuItem("Unblock Friend");
		else
			block = new JMenuItem("Block Friend");

		block.addActionListener(l);
		add(block);
	}

	public class Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == block) {
				if (model.getFriendList().isBlocked(user.getEmail())) {
					model.getServer().unblock(user.getEmail());
					model.getFriendList().removeBlockedUser(user);
				} else {
					model.getServer().block(user.getEmail());
					model.getFriendList().addBlockedUser(user);
				}

			} else if (e.getSource() == startChat) {

				if (GimClient.findRoom(user) == -1 && (!user.getStatus().equalsIgnoreCase("offline")))
					model.createRoom(user);
				else
					GimClient.getWindow(user).setVisible(true);

			} else if (e.getSource() == remove) {
				model.getServer().delete(user.getEmail());
				model.getFriendList().removeUser(user);

			} else if (e.getSource() == viewLogs) {
				// TODO: Open the logs

			}
		}

	}

}
