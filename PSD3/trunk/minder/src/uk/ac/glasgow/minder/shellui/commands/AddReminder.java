package uk.ac.glasgow.minder.shellui.commands;

import java.util.List;

import uk.ac.glasgow.minder.uistate.UIState;

public class AddReminder extends AbstractMinderCommand{

	public AddReminder(UIState uiState) {
		super(uiState);
	}

	@Override
	public String getUsage() {
		return "<eventid> <recipientid> <timeBefore>";
	}

	@Override
	public String invoke(List<String> arguments) throws Exception {
		String eventid =  arguments.get(0);
		String recipientid = arguments.get(1);
		
		Long timeBefore = Long.parseLong(arguments.get(2));
		
		if (	eventid != null 
				&& recipientid != null 
				&& timeBefore != null ){
			
			uiState.attachReminderToEvent(eventid,recipientid,timeBefore);
			return "Added reminder for event ["+eventid+"] for recipient ["+recipientid+"].";
			
		}else throw new Exception();
	}

}
