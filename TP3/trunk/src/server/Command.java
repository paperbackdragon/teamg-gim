package server;

public class Command {

	public enum COMMANDS {
		PING, PONG, AUTH, QUIT, GETSTATUS, STATUS, KILL, BROADCAST, SET, GET, FRIENDLIST, MESSAGE, FREIND, LOGOUT, FRIENDREQUEST, UPDATE, INFO, ERROR, DOESNOTEXIST
	}

	COMMANDS command;
	String[] arguments;
	String data;

	/**
	 * Default constructor
	 */
	public Command() {
		this(null, null, null);
	}

	/**
	 * Command constructor
	 * 
	 * @param cmd
	 * @param args
	 * @param data
	 */
	public Command(String cmd, String args, String data) {
		
		try {
			this.command = COMMANDS.valueOf(cmd.replaceAll(" ", "").toUpperCase());
			this.arguments = args.split(" ");
			this.data = data;
		} catch (IllegalArgumentException e) {
			this.command = COMMANDS.DOESNOTEXIST;
		}
	}

	public String toString() {
		String command = ":" + this.command.name();

		for(String arg : arguments) {
			command += " " + arg;
		}

		command += ":";

		if (data != null) {
			command += " " + data;
		}

		command += ";";

		return command;
	}

}
