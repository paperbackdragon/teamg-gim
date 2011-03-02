package uk.ac.glasgow.minder.shellui.commands;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import uk.ac.glasgow.minder.uistate.UIState;

public class CreateConference extends AbstractMinderCommand {

	public CreateConference(UIState uiState) {
		super(uiState);
	}

	@Override
	public String getUsage() {
		return "'<conference title>' '<dd>/<mm>/<yy>' '<dd>/<mm>/<yy>'";
	}

	@Override
	public String invoke(List<String> arguments) throws Exception {
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);

		String title = arguments.get(0);
		Date startDate = df.parse(arguments.get(1));
		Date endDate = df.parse(arguments.get(2));

		if (title != null && startDate != null && endDate != null) {
			uiState.createConference(title, startDate, endDate);
			return "Created conference [" + title + "].";
			
		} else
			throw new Exception();
	}

}
