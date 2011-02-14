package uk.ac.glasgow.minder.shellui.commands;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import uk.ac.glasgow.minder.uistate.UIState;

public class CreateDeadline extends AbstractMinderCommand {

	public CreateDeadline(UIState uiState) {
		super(uiState);
	}

	@Override
	public String getUsage() {
		return "'<dd>/<mm>/<yy>' '<hh>:<mm>' <deliverable <course>";
	}

	@Override
	public String invoke(List<String> arguments) throws Exception {
		DateFormat df = DateFormat.getInstance();

		Date date = null;
		String dateS = arguments.get(0) + " " + arguments.get(1);
		date = df.parse(dateS);

		String deliverable = arguments.get(2);
		String course = arguments.get(3);

		if (date != null && deliverable != null && course != null) {
			uiState.createDeadlineEvent(date, deliverable, course);
			return "Created deadline event for [" + deliverable + "].";
		} else
			throw new Exception();
	}

}
