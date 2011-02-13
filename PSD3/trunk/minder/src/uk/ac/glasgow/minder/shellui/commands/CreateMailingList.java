package uk.ac.glasgow.minder.shellui.commands;

import java.util.List;

import uk.ac.glasgow.minder.uistate.UIState;

public class CreateMailingList extends AbstractMinderCommand {

	public CreateMailingList(UIState uiState) {
		super(uiState);
	}

	@Override
	public String getUsage() {
		return "<label>";
	}

	@Override
	public String invoke(List<String> arguments) throws Exception {
		String label = arguments.get(0);
		
		if (label != null){
			uiState.createMailingList(label);		
			return "Creating mailing list ["+label+"].";
		}
		else throw new Exception();	
			
	}

}
