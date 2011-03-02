package uk.ac.glasgow.minder.shellui.commands;

import java.util.List;

import uk.ac.glasgow.minder.uistate.UIState;

public class AddToConference extends AbstractMinderCommand {

	public AddToConference(UIState uiState) {
		super(uiState);
	}

	@Override
	public String getUsage() {
		return "'<event id>' '<conference id>'";
	}

	@Override
	public String invoke(List<String> arguments) throws Exception {
		String event = arguments.get(0);
		String conference = arguments.get(1);

		if (event != null && conference != null) {
			uiState.createConference(event, conference);
			return "Added to conference [" + conference + "].";

		} else
			throw new Exception();
	}

}
