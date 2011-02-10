package client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import client.GimClient;
import client.Model;
import client.User;

public class GroupChatContextMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;

	Model model = Model.getInstance();

	JMenuItem startChat, add, remove, block;
	User user;

	public GroupChatContextMenu(User u) {
		this.user = u;

		if (u == model.getSelf()) {
			JMenuItem self = new JMenuItem("This is you");
			self.setEnabled(false);
			add(self);
			return;
		}

		Listener l = new Listener();

		if (model.getFriendList().containts(u)) {
			startChat = new JMenuItem("Start Chat...");
			startChat.addActionListener(l);

			if (user.getStatus().equalsIgnoreCase("offline") || model.getFriendList().isBlocked(user.getEmail()))
				startChat.setEnabled(false);

			add(startChat);
			add(new JSeparator());

			remove = new JMenuItem("Remove Friend");
			remove.addActionListener(l);
			add(remove);

		} else {
			add = new JMenuItem("Add Contact");
			add.addActionListener(l);
			add(add);
		}

		if (model.getFriendList().isBlocked(user.getEmail()))
			block = new JMenuItem("Unblock Contact");
		else
			block = new JMenuItem("Block Contact");

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

			} else if (e.getSource() == add) {
				model.getServer().add(user.getEmail());
			}
		}

	}

}
