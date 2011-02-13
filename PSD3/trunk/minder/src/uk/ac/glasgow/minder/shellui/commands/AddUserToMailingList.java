package uk.ac.glasgow.minder.shellui.commands;

import java.util.List;

import uk.ac.glasgow.minder.uistate.UIState;

public class AddUserToMailingList extends AbstractMinderCommand{

	public AddUserToMailingList(UIState uiState) {
		super(uiState);
	}

	@Override
	public String getUsage() {
		return "<username> <label>";
	}

	@Override
	public String invoke(List<String> arguments) throws Exception {
		String username = arguments.get(0);
		String label = arguments.get(1);
		
		if (username != null && label != null){
			uiState.addUserToMailingList(username,label);
			return "Added user ["+username+"] to mailing list ["+label+"].";
		} throw new Exception();

	}

}
