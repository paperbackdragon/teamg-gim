package client.net;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import client.GimClient;
import client.ui.ChatWindowIdentifier;
import client.ui.ClientModel;
import client.ui.ContactPanel;
import client.ui.GroupChatPanel;
import client.ui.LoginPanel;
import client.ui.MainWindow;
import client.ui.SingleChatPanel;
import client.ui.User;

public class ServerConnection implements NetworkingIn {

	private ClientModel model;

	public ServerConnection(ClientModel model) {
		this.model = model;
	}

	public void authorised() {
		model.getFriendList();

		// Do this properly later...
		// may want to sign in appearing offline
		// GimClient.getClient().getOwnUserName()

		model.setStatus("ONLINE");

		model.getNickName(model.getOwnUserName());
		model.getPersonalMessage(model.getOwnUserName());

		ContactPanel panel = GimClient.getContactPanel();
		GimClient.getMainWindow().setMainPanel(panel);
		GimClient.getMainWindow().canLogout(true);
	}

	public void broadcast(String message) {
	}

	public void emailInuseError(String message) {
		// JOptionPane.showMessageDialog(GimClient.getMainWindow(),
		// "E-Mail is already in use.");
		JOptionPane.showMessageDialog(GimClient.getMainWindow(), message);
	}

	public void registered() {
		LoginPanel panel = new LoginPanel();
		panel.setParent(GimClient.getMainWindow());
		GimClient.getMainWindow().setMainPanel(panel);
	}

	public void friendlist(String[] onlinelist, String[] offlinelist, String[] blockedlist) {

		model.setOnlinefriends(onlinelist);
		model.setOfflinefriends(offlinelist);
		model.setBlockedfriends(blockedlist);

		// update the interface

		for (int i = 0; i < onlinelist.length; i++) {
			if (model.getUser(onlinelist[i]) == null) {
				model.addUser(onlinelist[i]);
				// GimClient.getClient().getUser(onlinelist[i])
				// .setStatus("Online");

				model.getStatus(onlinelist[i]);
				model.getPersonalMessage(onlinelist[i]);
				model.getNickName(onlinelist[i]);
			}

		}
		GimClient.getContactPanel().createNodes(onlinelist, offlinelist);

	}

	public void friendrequest(String user, String nickname) {
		Object[] options = { "Accept", "Decline", };
		int n = JOptionPane.showOptionDialog(null, "You have received a friend request from " + user + "(nickname :"
				+ nickname + ".) Would you like to accept?", "Freind Request", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		if (n == 0) {
			model.acceptRequest(user);
		}

	}

	public void invalidArgumentError(String message) {
	}

	public void invalidEmailError(String message) {
	}

	public void kill(String message) {
	}

	public void logInDetailsIncorrectError(String message) {
		String error = "";
		if (!message.equals("")) {
			error += ":\n\n server reported:\n " + message;
		}

		JOptionPane.showMessageDialog(GimClient.getMainWindow(), "Log in Details Incorrect." + error);
	}

	public void loggedInFromAnotherLocationError(String message) {
		String error = "";
		if (!message.equals("")) {
			error += ":\n\n server reported:\n " + message;
		}

		JOptionPane.showMessageDialog(GimClient.getMainWindow(), "Logged in from another location." + error);
	}

	public void message(String roomid, String sender, String message) {
		GimClient.routeMessage(roomid, sender, message);
	}

	public void missingArgumentsError(String message) {

	}

	public void notifyDisplayPicture(String user) {
		model.getDisplayPicture(user);
	}

	public void notifyNickname(String user) {
		model.getNickName(user);

	}

	public void notifyPersonalMessage(String user) {
		model.getPersonalMessage(user);
	}

	public void notifyStatus(String user) {
		// the user might have gone offline, tell the server
		// to send an updated buddy list
		// (this may change if the server sends it anyway...
		// need to talk to james on that one)
		model.getFriendList();
		model.getStatus(user);
	}

	public void okay() {
	}

	public void passwordTooShortError(String message) {
		String error = "";
		if (!message.equals("")) {
			error += ":\n\n server reported :\n " + message;
		}

		JOptionPane.showMessageDialog(GimClient.getMainWindow(), "Password too short error." + error);
	}

	public void servertime(String servertime) {

	}

	// don't think this one will exist... will need to ask james!
	/*
	 * @Override public void status(String status) {
	 * 
	 * }
	 */

	public void tooManyArgumentsError(String message) {

	}

	public void unauthorised() {

	}

	public void unauthorisedError(String message) {

	}

	public void updateDisplayPicture(String user, String displayPicture) {
		if (!user.equals(model.getOwnUserName())) {

			if (model.getUser(user) != null) {
				User l = model.getUser(user);
				if (l != null) {
					l.setDisplayPic(displayPicture);
					// lul'z: dunno how to get this into an imageIcon
				}

			} else {
				// uhm...
			}
		}

		else {
			// set own display pic
		}

	}

	public void updateNickname(String user, String nickname) {
		if (!user.equals(model.getOwnUserName())) {

			User l = model.getUser(user);
			if (l != null) {
				l.setNickname(nickname);

				ChatWindowIdentifier s = GimClient.getWindowIdentifierFromUser(user);

				if (s != null) {
					((SingleChatPanel) s.getCp()).setNickname(nickname);
				}
			}

		}

		else {
			model.setOwnNickname(nickname);
			GimClient.getContactPanel().setMyNickname(nickname);
		}

		// update in any group chats
		GimClient.updateGroupChatLists();

	}

	public void updatePersonalMessage(String user, String personalmessage) {
		if (!user.equals(model.getOwnUserName())) {

			User l = model.getUser(user);
			if (l != null) {
				l.setPersonalMessage(personalmessage);

				ChatWindowIdentifier s = GimClient.getWindowIdentifierFromUser(user);

				if (s != null) {
					((SingleChatPanel) s.getCp()).setPersonalMessage(personalmessage);
				}
			}
		} else {
			model.setOwnPersonalMessage(personalmessage);
			GimClient.getContactPanel().setMyPersonalMessage(personalmessage);
		}

		// update in any group chats
		GimClient.updateGroupChatLists();
	}

	public void updateStatus(String user, String status) {
		if (!user.equals(model.getOwnUserName())) {

			User l = model.getUser(user);
			if (l != null) {
				l.setStatus(status);

				ChatWindowIdentifier s = GimClient.getWindowIdentifierFromUser(user);

				if (s != null) {
					((SingleChatPanel) s.getCp()).setStatus(status);
				}
			}
		} else {
			GimClient.getContactPanel().setMyStatus(status);

		}

		// update in any group chats
		GimClient.updateGroupChatLists();

	}

	public void uptime(String uptime) {

	}

	public void userDoesNotExistError(String message) {
		String error = "";
		if (!message.equals("")) {
			error += ":\n\n server reported :\n " + message;
		}

		JOptionPane.showMessageDialog(GimClient.getMainWindow(), "User does not exist error. " + error);
	}

	public void userAlreadyinFriendlistError(String message) {
		String error = "";
		if (!message.equals("")) {
			error += ":\n\n server reported :\n " + message;
		}

		JOptionPane.showMessageDialog(GimClient.getMainWindow(), "User already in friendslist. " + error);

	}

	public void usercount(String usercount) {

	}

	public void created(final String roomid) {
		// get next list of users

		final String[] contacts = model.getNextRoom();
		final Boolean isGroup = model.getNextType();

		// open new chat window
		if (isGroup) {

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					GroupChatPanel gcp = new GroupChatPanel(roomid);
					// GimClient.addRoom(gcp);
					gcp.setInProgress(true);

					MainWindow ui = new MainWindow("GIM - Group Chat", gcp);
					GimClient.addWindow(" ", ui, gcp);

					ui.setLocationRelativeTo(null);// center new chat window
					model.users(roomid);
					ui.setVisible(true);
				}
			});

		} else {

			// if we already have a window...
			ChatWindowIdentifier l = GimClient.getWindowIdentifierFromUser(contacts[0]);
			if (l != null) {
				l.getCp().setId(roomid);
				// l.getCp().setInProgress(true); // find a better way

			} else {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						SingleChatPanel scp = new SingleChatPanel(roomid);

						// set the chat to be with the user we invited to chat
						scp.setChatWith(contacts[0]);
						// scp.setInProgress(true);
						// </Gordon>

						// GimClient.addRoom(scp);

						// get contact info
						User l = model.getUser(contacts[0]);
						if (l != null) {
							scp.setNickname(l.getNickname());
							scp.setStatus(l.getStatus());
							scp.setPersonalMessage(l.getPersonalMessage());
						}

						MainWindow ui = new MainWindow("GIM - Chat with " + contacts[0], scp);
						GimClient.addWindow(contacts[0], ui, scp);

						ui.setLocationRelativeTo(null);// center new chat window
						ui.setVisible(true);
					}
				});
			}

		}

		// invite contacts
		for (String s : contacts) {
			model.invite(roomid, s);
		}

	}

	public void invited(String user, String roomid) {
		model.type(roomid);

		// Store who gave the invitation, for when we ask the user if they want
		// to join
		model.addInvitation(user);

		/*
		 * Gordon: it could be possible to display the list of users already in
		 * the room with this invitation... but that would be a bit of work.
		 * discuss? could be a low priority...
		 */
	}

	public void joined(final String user, final String roomid) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				System.out.println("got to the joined method");
				ChatWindowIdentifier l = GimClient.getWindowIdentifierFromId(roomid);

				// The other person has joined the personal chat, it is safe to
				// send messages
				if (l != null) {
					if (l.getCp() instanceof SingleChatPanel) {
						l.getCp().setInProgress(true);
					} else { // group chat
						model.users(roomid);
						l.getCp().receiveMessage("", user + " has joined the chat\n");

					}

				}

				else { // they joined a group chat
					if (model.getUser(user) == null) {
						model.addUser(user);

						model.getNickName(user);
						model.getStatus(user);
						model.getPersonalMessage(user);
					}
				}

			}
		});

	}

	public void left(String user, String roomid) {
		// this used to be FromUser... this change shouldn't screw anything up
		// as far as i know
		// but this is a reminder to myself...
		ChatWindowIdentifier l = GimClient.getWindowIdentifierFromId(roomid);
		if (l != null) {
			if (l.getCp() instanceof SingleChatPanel) {
				l.getCp().setInProgress(false);
				l.getCp().setId("-1");
				model.leave(roomid);
			} else if (l.getCp() instanceof GroupChatPanel) {
				// do this later...
				GimClient.getWindowIdentifierFromId(roomid).getCp().receiveMessage("", user + " has left the chat\n");
				model.users(roomid);
			}

		}

	}

	public void users(String[] users, String roomid) {
		// could work out their nickname... but screw it, do it later

		((GroupChatPanel) GimClient.getWindowIdentifierFromId(roomid).getCp()).updateUserList(users);

	}

	public void group(final String roomid) {
		String invitedBy = model.getNextInvitation();

		// spawn a new window notifying the user they've been invited to a group
		// chat

		Object[] options = { "Accept", "Decline" };
		int n = JOptionPane.showOptionDialog(GimClient.getMainWindow(), "You have been invited to a group chat by "
				+ invitedBy + ". Would you like to accept?", "Group chat invitation", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, // the titles of
				// buttons
				options[0]); // default button title

		if (n == 0) {
			model.join(roomid);

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					GroupChatPanel gcp = new GroupChatPanel(roomid);
					// GimClient.addRoom(gcp);

					MainWindow ui = new MainWindow("GIM - Group Chat", gcp);
					GimClient.addWindow(" ", ui, gcp);
					ui.setVisible(true);
					gcp.setInProgress(true);

					ui.setLocationRelativeTo(null);// center new chat window
					model.users(roomid);
				}
			});
		}

	}

	public void personal(final String roomid) {
		System.out.println("someone invited us to a personal chat");

		/*
		 * gordon: spawn a personal chat window immediately ... oh wait, what if
		 * no message has been sent yet... ... we might need to keep a log of
		 * messages, and make the IM window visable ... only when a message has
		 * been received. This should only be a few lines in the chat window
		 * handler class. Like, when the "write message to window" method is
		 * invoked, if messagecount == 0, spawn a window...
		 */
		final String invitedBy = model.getNextInvitation();

		model.join(roomid);

		ChatWindowIdentifier l = GimClient.getWindowIdentifierFromUser(invitedBy);
		if (l != null) {
			l.getCp().setId(roomid);
			l.getCp().setInProgress(true); // find a better way later
		} else { // there is no window for this user
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					SingleChatPanel scp = new SingleChatPanel(roomid);
					// gordon
					scp.setChatWith(invitedBy);
					scp.setInProgress(true);

					// set contact info

					User l = model.getUser(invitedBy);
					if (l != null) {
						scp.setNickname(l.getNickname());
						scp.setStatus(l.getStatus());
						scp.setPersonalMessage(l.getPersonalMessage());
					}
					// </gordon>
					// GimClient.addRoom(scp);
					MainWindow ui = new MainWindow("GIM - Chat with " + invitedBy, scp);
					GimClient.addWindow(invitedBy, ui, scp);
					ui.setLocationRelativeTo(null);// center new chat window
				}
			});
		}
	}

	public void notifyFriendsList() {
		model.getFriendList();
	}

	@Override
	public void invalidUserError(String message) {
		// FIX THIS LATER TO PARSE FOR CONTEXT. FOR NOW, ASSUME
		// USER HAS DOUBLE CLICKED 'ONLINE' on buddy list

		// the invitatiation we queued up was invalid
		model.getNextRoom();

	}

	@Override
	public void userOfflineError(String message) {
		// FIX THIS LATER TO PARSE FOR CONTEXT. FOR NOW, ASSUME
		// USER HAS DOUBLE CLICKED AN OFFLINE USER on buddy list

		// the invitation we queued up was invalid
		model.getNextRoom();
	}

	public void connectionDroppedError() {
		JOptionPane.showMessageDialog(GimClient.getMainWindow(), "Connection to server lost. Try logging in again. ");
	}

}
