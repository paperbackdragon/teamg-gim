package uk.ac.glasgow.minder.shellui.commands;

import java.util.List;

import javax.mail.internet.InternetAddress;

import uk.ac.glasgow.minder.recipient.Privilege;

import uk.ac.glasgow.minder.uistate.UIState;

public class CreateUser extends AbstractMinderCommand {

	public CreateUser(UIState uiState) {
		super(uiState);
	}

	@Override
	public String getUsage() {
		return "createuser '<display name>' <username> <password> <email-address> <ADMINISTRATOR|EVENTMANAGER|RECIPIENT>";
	}

	@Override
	public String invoke(List<String> arguments) throws Exception {
		String displayName = arguments.get(0);
		String username = arguments.get(1);
		String password = arguments.get(2);

		InternetAddress mailAddress = new InternetAddress(arguments.get(3));
		
		Privilege privilege = Privilege.valueOf(arguments.get(4));

		if (displayName !=null 
				&& username != null 
				&& password != null 
				&& mailAddress != null 
				&& privilege != null){

			uiState.createUser(displayName,	username, password, mailAddress, privilege);
			return "added user ["+username+"]";
		}
		else throw new Exception();
			
	}

}



