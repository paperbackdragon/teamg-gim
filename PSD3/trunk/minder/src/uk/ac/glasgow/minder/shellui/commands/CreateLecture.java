package uk.ac.glasgow.minder.shellui.commands;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import uk.ac.glasgow.minder.uistate.UIState;

public class CreateLecture extends AbstractMinderCommand {

	public CreateLecture(UIState uiState) {
		super(uiState);
	}

	@Override
	public String getUsage() {
		return "'<dd>/<mm>/<yy>' '<hh>:<mm>' '<location>' <lecturer username> <duration> '<title>'";
	}

	@Override
	public String invoke(List<String> arguments) throws Exception {
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.UK);

		Date date = null;
		String dateS = arguments.get(0) + " " + arguments.get(1);
		date = df.parse(dateS);

		String location = arguments.get(2);
		String lecturerUsername = arguments.get(3);
		Long duration = Long.parseLong(arguments.get(4));
		String title = arguments.get(5);

		if (date != null && location != null && lecturerUsername != null && duration != null && title != null) {

			uiState.createLectureEvent(date, location, lecturerUsername, duration, title);
			return "Created lecture event [" + title + "].";
		} else
			throw new Exception();
	}

}
