package client.net;

import java.util.ArrayList;

import client.GimClient;
import client.ui.ContactPanel;

public class ServerConnection implements NetworkingIn {

	@Override
	public void authorised() {
		ContactPanel panel = new ContactPanel();
		panel.setParent(GimClient.getMainWindow());
		GimClient.getMainWindow().setMainPanel(panel);
	}

	@Override
	public void broadcast(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void emailInuseError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void friendlist(ArrayList<String> onlinelist,
			ArrayList<String> offlinelist, ArrayList<String> blockedlist) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void friendrequest(String user, String nickname) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void invalidArgumentError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void invalidEmailError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void kill(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logInDetailsIncorrectError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loggedInFromAnotherLocationError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void message(String roomid, String sender, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void missingArgumentsError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyDisplayPicture(String user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyNickname(String user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyPersonalMessage(String user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyStatus(String user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void okay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void passwordTooShortError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void servertime(String servertime) {
		// TODO Auto-generated method stub
		
	}

	
	// don't think this one will exist... will need to ask james!
/*	@Override
	public void status(String status) {
		// TODO Auto-generated method stub
		
	}*/

	@Override
	public void tooManyArgumentsError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unauthorised() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unauthorisedError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateDisplayPicture(String user, String displayPicture) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNickname(String user, String nickname) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePersonalMessage(String user, String personalmessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateStatus(String user, String status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uptime(String uptime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void userDoesNotExistError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void usercount(String usercount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void created(String roomid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void invited(String user, String roomid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void joined(String user, String roomid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void left(String user, String roomid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void users(ArrayList<String> users, String roomid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registered() {
		// TODO Auto-generated method stub
		
	}

	
	
	
}
