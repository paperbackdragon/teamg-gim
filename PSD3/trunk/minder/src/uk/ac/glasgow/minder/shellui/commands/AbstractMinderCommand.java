package uk.ac.glasgow.minder.shellui.commands;

import uk.ac.glasgow.minder.shellui.Command;
import uk.ac.glasgow.minder.uistate.UIState;

public abstract class AbstractMinderCommand implements Command {

	protected UIState uiState;
	
	public AbstractMinderCommand(UIState uiState){
		this.uiState = uiState;
	}
}
