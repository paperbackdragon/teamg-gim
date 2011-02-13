package uk.ac.glasgow.minder.shellui.commands;

import java.util.List;
import java.util.Set;

import uk.ac.glasgow.minder.recipient.Recipient;
import uk.ac.glasgow.minder.uistate.UIState;

public class SearchRecipients extends AbstractMinderCommand{

	public SearchRecipients(UIState uiState) {
		super(uiState);
	}

	@Override
	public String getUsage() {
		return "'<pattern>'";
	}

	@Override
	public String invoke(List<String> arguments) throws Exception {
		Set<Recipient> recipients = uiState.searchRecipients(arguments.get(0));
		
		String result = "";
		if (recipients != null) 
			for (Recipient recipient: recipients)
				result+=recipient.toString()+"\n";
		
		return result;
	}
}
