package uk.ac.glasgow.minder;

import javax.mail.internet.InternetAddress;
import uk.ac.glasgow.minder.event.EventHost;
import uk.ac.glasgow.minder.event.impl.Controller;
import uk.ac.glasgow.minder.recipient.RecipientStore;
import uk.ac.glasgow.minder.recipient.Privilege;
import uk.ac.glasgow.minder.recipient.impl.RecipientStoreImpl;
import uk.ac.glasgow.minder.shellui.ShellUI;
import uk.ac.glasgow.minder.uistate.UIState;
import uk.ac.glasgow.minder.uistate.impl.UIStateImpl;

import uk.ac.glasgow.minder.shellui.commands.*;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		RecipientStore rs = new RecipientStoreImpl();
			rs.addUser("Administrator", "admin", "monkey",
					new InternetAddress("monkey.me@glasgow.ac.uk"), Privilege.ADMINISTRATOR);

		
		EventHost host = new Controller(rs);
		
		UIState uiState = new UIStateImpl(rs, host);
		
		ShellUI shellUI = new ShellUI(System.in,System.out,System.err,"minder");
		
		shellUI.registerCommand("login",new Login(uiState));
		shellUI.registerCommand("createuser",new CreateUser(uiState));
		shellUI.registerCommand("createml",new CreateMailingList(uiState));
		shellUI.registerCommand("adduserml",new AddUserToMailingList(uiState));
		shellUI.registerCommand("searchrecipients",new SearchRecipients(uiState));
		shellUI.registerCommand("searchevents",new SearchEvents(uiState));
		shellUI.registerCommand("addreminder",new AddReminder(uiState));
		shellUI.registerCommand("createlecture",new CreateLecture(uiState));
		shellUI.registerCommand("createdeadline",new CreateDeadline(uiState));
		shellUI.registerCommand("exit",new Exit(uiState));
		shellUI.run();
	}

}
