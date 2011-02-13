package uk.ac.glasgow.minder.shellui.commands;

import java.util.List;

import uk.ac.glasgow.minder.uistate.UIState;

public class Exit extends AbstractMinderCommand {

	public Exit(UIState uiState) {
		super(uiState);
	}

	@Override
	public String getUsage() {
		return "";
	}

	@Override
	public String invoke(List<String> arguments) throws Exception {
		System.exit(0);
		return "exiting...";
	}

}
