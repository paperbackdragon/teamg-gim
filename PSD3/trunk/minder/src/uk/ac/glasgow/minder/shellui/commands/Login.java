package uk.ac.glasgow.minder.shellui.commands;

import java.util.List;

import uk.ac.glasgow.minder.recipient.User;
import uk.ac.glasgow.minder.uistate.UIState;

public class Login extends AbstractMinderCommand{

	public Login(UIState uiState) {
		super(uiState);
		
	}

	@Override
	public String invoke(List<String> arguments) throws Exception {
		String username = arguments.get(0);
		String password = arguments.get(1);
				
		User user = uiState.login(username, password);
		if (user != null)
			return "current user is ["+user.getDisplayName()+"].";
		else throw new Exception();
	}
	
	/** 
	 * @see uk.ac.glasgow.minder.shellui.Command#getUsage()
	 */
	@Override
	public String getUsage(){
		return "<username> <password>";
	}
}
