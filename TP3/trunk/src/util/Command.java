package util;

public class Command {

	// All of the valid commands in the protocol
	public static enum COMMANDS {
		PING, SERVER, AUTH, QUIT, OKAY, SERVERSTATUS, STATUS, KILL, BROADCAST, SET, GET, FRIENDLIST, ROOM, MESSAGE, FREINDREQUEST, FRIEND, LOGOUT, UPDATE, INFO, ERROR, DOESNOTEXIST
	}

	private COMMANDS command = null;
	private String[] arguments = null;
	private String data = null;

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
		if (args != null)
			this.arguments = args.split(" ");
		else
			this.arguments = new String[0];

		// We don't like null.
		if (data != null)
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
	 * Get the command as an ENUM value
	 * 
	 * @return The command
	 */
	public COMMANDS getCommandAsEnum() {
		return this.command;
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

	/**
	 * Get the arguments for the command
	 * 
	 * @return an array of the arguments
	 */
	public String[] getArguments() {
		return this.arguments;
	}

	/**
	 * Get the arguments for the command as a String
	 * 
	 * @return A string of space separated arguments
	 */
	public String getArumentsAsString() {
		String args = "";
		for (int i = 0; i < arguments.length; i++) {
			args += arguments[i];
			if (i < (arguments.length - 1))
				args += " ";
		}
		return args;
	}

	/**
	 * Set the arguments
	 * 
	 * @param args
	 *            A string of space separated arguments
	 */
	public void setArguments(String args) {
		if (args != null)
			this.arguments = args.split(" ");
		else
			this.arguments = null;
	}

	/**
	 * Set the arguments
	 * 
	 * @param args
	 *            An array of arguments
	 */
	public void setArguments(String[] args) {
		this.arguments = args;
	}

	/**
	 * Get the data for the command
	 * 
	 * @return The data
	 */
	public String getData() {
		return this.data;
	}

	/**
	 * Set the data for the command
	 * 
	 * @param data
	 *            the data
	 */
	public void setData(String data) {
		this.data = data;
	}
	
	static public String byteToHex(byte b) {
		// Returns hex String representation of byte b
		char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
		return new String(array);
	}

	static public String charToHex(char c) {
		// Returns hex String representation of char c
		byte hi = (byte) (c >>> 8);
		byte lo = (byte) (c & 0xff);
		return byteToHex(hi) + byteToHex(lo);
	}

	public static String unicodeEncode(String s) {
		String encoded = "";
		for (Character c : s.toCharArray()) {
			if(!Character.isLetterOrDigit(c))
				encoded += "\\U+" + charToHex(c);
			else
				encoded += c;
		}
		return encoded;
	}

	/**
	 * Convert the command into a well formated command
	 */
	public String toString() {
		String command = ":" + this.command.name();

		for (String arg : arguments) {
			command += " " + arg;
		}

		command += ":";

		if (data != null) {
			command += data;
		}

		command += ";";

		return command;
	}

}
