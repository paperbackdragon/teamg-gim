package uk.ac.glasgow.minder.shellui;

import java.util.List;

public interface Command {
	public String invoke(List<String> arguments) throws Exception;

	public String getUsage();
}
