package util;

public class Command {

	// All of the valid commands in the protocol
	public static enum COMMANDS {
		PING, SERVER, AUTH, QUIT, OKAY, SERVERSTATUS, KILL, BROADCAST, SET, GET, FRIENDLIST, ROOM, MESSAGE, FREINDREQUEST, FRIEND, LOGOUT, UPDATE, INFO, ERROR, DOESNOTEXIST
	}

	private COMMANDS command = null;
	private String[] arguments = new String[0];
	private String data = "";

	/**
	 * Default constructor, creates a DOESNOTEXIST command
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

		// We don't like null.
		if (data != null)
			this.data = data;
	}

	/**
	 * Constructor for a command which has no data, only arguments
	 * 
	 * @param cmd
	 *            The type of COMMAND which this command is
	 * @param args
	 *            The arguments of the command
	 */
	public Command(String cmd, String args) {
		// Make sure the command is one that we recognise
		try {
			this.command = COMMANDS.valueOf(cmd.replaceAll(" ", "").toUpperCase());
		} catch (IllegalArgumentException e) {
			this.command = COMMANDS.DOESNOTEXIST;
		}

		// Gota make sure there's an array and not just null
		if (args != null)
			this.arguments = args.split(" ");

	}

	/**
	 * Constructor for a command with no arguments or data
	 * 
	 * @param cmd
	 *            The type of COMMAND which this command is
	 */
	public Command(String cmd) {
		// Make sure the command is one that we recognise
		try {
			this.command = COMMANDS.valueOf(cmd.replaceAll(" ", "").toUpperCase());
		} catch (IllegalArgumentException e) {
			this.command = COMMANDS.DOESNOTEXIST;
		}
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
	public String getArgumentsAsString() {
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
	 * Get the decoded data
	 * 
	 * @return the data
	 */
	public String getDecodedData() {
		return decode(this.data);
	}

	/**
	 * Split the data and then decode it
	 * 
	 * @param split
	 *            The regex showing where to split the code (same as
	 *            String.split();)
	 * @return The split and decoded data
	 */
	public String[] splitAndDecodeData(String split) {
		if (data == null)
			return new String[0];

		String[] parts = getData().split(split);
		for (int i = 0; i < parts.length; i++)
			parts[i] = decode(parts[i]);

		return parts;

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

		return command + ";";
	}

	/**
	 * Convert a byte to its hex representation
	 * 
	 * @param b
	 *            the byte to convert
	 * @return The hex representation of the byte
	 */
	private static String byteToHex(byte b) {
		// Returns hex String representation of byte b
		char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
		return new String(array);
	}

	/**
	 * Convert a character to its hex representation
	 * 
	 * @param c
	 *            The character to convert
	 * @return The hex representation of the byte
	 */
	private static String charToHex(char c) {
		// Returns hex String representation of char c
		byte hi = (byte) (c >>> 8);
		byte lo = (byte) (c & 0xff);
		return byteToHex(hi) + byteToHex(lo);
	}

	/**
	 * Encode a string so that any no Letter or Digit is converted to a Unicode
	 * character represented by its hex
	 * 
	 * @param s
	 *            The string to encode
	 * @return The encoded string
	 */
	public static String encode(String s) {
		String encoded = "";
		for (Character c : s.toCharArray()) {
			if (!Character.isLetterOrDigit(c))
				encoded += "\\U+" + charToHex(c);
			else
				encoded += c;
		}
		return encoded;
	}

	/**
	 * Decode a string where any pattern matching \U+XXXX is converted to its
	 * Unicode character
	 * 
	 * @param s
	 *            The string to decode
	 * @return The decoded string
	 * 
	 *         TODO: Reject badly formatted input...or just catch the exception?
	 */
	public static String decode(String s) {
		String decoded = "";

		int pos = s.indexOf("\\");
		while (pos != -1) {
			// Add everything up the first \ to the decoded part of the string
			decoded += s.substring(0, pos);

			// Convert the next 7 characters to the Unicode character
			int val = Integer.parseInt(s.substring(pos + 3, pos + 7), 16);
			decoded += new Character((char) val);

			// Strip the string were decoding up to the point where we've
			// decoded
			s = s.substring(pos + 7);
			pos = s.indexOf("\\");
		}
		decoded += s;

		return decoded;
	}

}
