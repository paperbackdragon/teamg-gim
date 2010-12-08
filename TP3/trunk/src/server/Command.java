package server;

import java.lang.reflect.Array;

public class Command {

	// All of the valid commands in the protocol
	public enum COMMANDS {
		PING, PONG, AUTH, QUIT, GETSTATUS, STATUS, KILL, BROADCAST, SET, GET, FRIENDLIST, MESSAGE, FREIND, LOGOUT, FRIENDREQUEST, UPDATE, INFO, ERROR, DOESNOTEXIST
	}

	COMMANDS command = null;
	String[] arguments = null;
	String data = null;

	/**
	 * Default constructor, creates an empty command.
	 */
	public Command() {
		this("DOESNOTEXIST", null, null);
	}

	/**
	 * Command constructor. If the command is not recognised as a valid command
	 * then a DOESNOTEXIST command is generated instead.
	 * 
	 * @param cmd
	 *            The type of command.
	 * @param args
	 *            The arguments separated by a space
	 * @param data
	 *            The data for the command
	 */
	public Command(String cmd, String args, String data) {
		// Make sure the command is one that we recognise
		try {
			this.command = COMMANDS.valueOf(cmd.replaceAll(" ", "").toUpperCase());
		} catch (IllegalArgumentException e) {
			this.command = COMMANDS.DOESNOTEXIST;
		}

		// Gota make sure there's an array and not just null
		if(args != null)
			this.arguments = args.split(" ");
		else
			this.arguments = new String[0];
		
		// We don't like null.
		if(data != null)
			this.data = data;
		else
			this.data = "";
	}

	/**
	 * Get the command
	 * 
	 * @return The command as a string
	 */
	public String getCommand() {
		return this.command.toString();
	}

	/**
	 * Set the command
	 * 
	 * @param cmd
	 *            The command as a string. If the command is not recognised then
	 *            the command is changed to a DOESNOTEXIST command.
	 */
	public void setCommand(String cmd) {
		// Make sure the command is one that we recognise
		try {
			this.command = COMMANDS.valueOf(cmd.replaceAll(" ", "").toUpperCase());
		} catch (IllegalArgumentException e) {
			this.command = COMMANDS.DOESNOTEXIST;
		}
	}

	public String[] getArguments() {
		return this.arguments;
	}

	public String getArgsAsString() {
		String args = "";
		for (int i = 0; i < arguments.length; i++) {
			args += arguments[i];
			if (i < (arguments.length - 1))
				args += " ";
		}
		return args;
	}

	/**
	 * Convert the command into a well formated command as a string.
	 */
	public String toString() {
		String command = ":" + this.command.name();

		for (String arg : arguments) {
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
