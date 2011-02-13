package uk.ac.glasgow.minder.shellui.commands;

import java.util.List;
import java.util.Set;

import uk.ac.glasgow.minder.event.Event;
import uk.ac.glasgow.minder.uistate.UIState;

public class SearchEvents extends AbstractMinderCommand{

	public SearchEvents(UIState uiState) {
		super(uiState);
	}

	@Override
	public String getUsage() {
		return "'<pattern>'";
	}

	@Override
	public String invoke(List<String> arguments) throws Exception {
		Set<Event> events = uiState.searchEvents(arguments.get(0));
		String result = "";
		if (events != null) 
			for (Event event: events)
				result+=event.toString()+"\n";
		
		return result;
	}

}
