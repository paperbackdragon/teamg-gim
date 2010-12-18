package client.net;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;


import client.*;
import client.ui.*;

public class ServerConnection implements NetworkingIn, Runnable {
	public void run() {
		
	}
	
	public void authorised() {
		ContactPanel panel = new ContactPanel();
		panel.setParent(GimClient.getMainWindow());
		GimClient.getMainWindow().setMainPanel(panel);
		GimClient.getMainWindow().canLogout(true);
	}

	public void broadcast(String message) {
		
	}

	public void emailInuseError(String message) {
		JOptionPane.showMessageDialog(GimClient.getMainWindow(), "E-Mail is already in use.");
	}
	
	public void registered() {
		LoginPanel panel = new LoginPanel();
		panel.setParent(GimClient.getMainWindow());
		GimClient.getMainWindow().setMainPanel(panel);
	}

	public void friendlist(String[] onlinelist,
			String[] offlinelist, String[] blockedlist) {
		
	}

	public void friendrequest(String user, String nickname) {
		
	}

	public void invalidArgumentError(String message) {
		
	}

	public void invalidEmailError(String message) {
		
	}

	public void kill(String message) {
		
	}

	public void logInDetailsIncorrectError(String message) {
		JOptionPane.showMessageDialog(GimClient.getMainWindow(), "Login details incorrect.");
	}

	public void loggedInFromAnotherLocationError(String message) {
		JOptionPane.showMessageDialog(GimClient.getMainWindow(), "Already logged in at another location.");
	}

	public void message(String roomid, String sender, String message) {
		
	}

	public void missingArgumentsError(String message) {
		
	}

	public void notifyDisplayPicture(String user) {
		
	}

	public void notifyNickname(String user) {
		
	}

	public void notifyPersonalMessage(String user) {
		
	}

	public void notifyStatus(String user) {
		
	}

	public void okay() {
		
	}

	public void passwordTooShortError(String message) {
		JOptionPane.showMessageDialog(GimClient.getMainWindow(), "Password is too short.");
	}

	public void servertime(String servertime) {
		
	}

	// don't think this one will exist... will need to ask james!
/*	@Override
	public void status(String status) {
		
	}*/

	public void tooManyArgumentsError(String message) {
		
	}

	public void unauthorised() {
		
	}

	public void unauthorisedError(String message) {
		
	}

	public void updateDisplayPicture(String user, String displayPicture) {
		
	}

	public void updateNickname(String user, String nickname) {
		
	}

	public void updatePersonalMessage(String user, String personalmessage) {
		
	}

	public void updateStatus(String user, String status) {
		
	}

	public void uptime(String uptime) {
		
	}

	public void userDoesNotExistError(String message) {
		
	}

	public void usercount(String usercount) {
		
	}

	public void created(final String roomid) {
		//get next list of users
		String[] contacts = GimClient.getClient().getRoomList().remove();
		
		//open new chat window
		if (contacts.length > 1) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					GroupChatPanel gcp = new GroupChatPanel(roomid);
					GimClient.addRoom(gcp);
					GimUI ui = new GimUI("GIM - Group Chat", gcp);
					ui.setLocationRelativeTo(null);//center new chat window
				}
			});	
		}
 		else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					SingleChatPanel scp = new SingleChatPanel(roomid);
					GimClient.addRoom(scp);
					GimUI ui = new GimUI("GIM - Chat with Contact ", scp);
					ui.setLocationRelativeTo(null);//center new chat window
				}
			});
		}
		
		//invite contacts
		for(String s : contacts) {
			GimClient.getClient().invite(roomid, s);
		}
		
	}

	public void invited(String user, String roomid) {
		
	}

	public void joined(String user, String roomid) {
		// TODO What happens when it goes from 2->3 people or vice versa?
	}

	public void left(String user, String roomid) {
		
	}

	public void users(ArrayList<String> users, String roomid) {
		
	}

	@Override
	public void group(String roomid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void personal(String roomid) {
		// TODO Auto-generated method stub
		
	}
}
