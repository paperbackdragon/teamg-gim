package uk.ac.glasgow.minder.shellui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A shell based user interface for the minder system.
 * 
 * @author tws
 * 
 */
public class ShellUI {

	private StreamTokenizer st;

	private PrintWriter out;
	private PrintWriter err;

	private Map<String, Command> commands = new HashMap<String, Command>();

	private String title;

	public ShellUI(InputStream in, OutputStream out, OutputStream err,
			String title) {
		this.title = title;
		st = new StreamTokenizer(new BufferedReader(new InputStreamReader(in)));
		st.quoteChar('\'');
		st.eolIsSignificant(true);
		st.wordChars('@', '@');

		this.out = new PrintWriter(out);
		this.err = new PrintWriter(err);

		this.commands.put("help", new Help());

	}

	public void registerCommand(String string, Command command) {
		commands.put(string, command);
	}

	public void run() {
		System.out.println("Welcome to " + title + ".  Type 'help' for usage.");
		while (true) {
			try {
				st.nextToken();
			} catch (IOException e) {
				err.print("Exception while reading command");
				e.printStackTrace();
			}

			if (st.sval != null) {

				String commandName = st.sval;
				Command c = commands.get(commandName);
				if (c != null) {
					List<String> args = null;
					try {
						args = processArguments();

					} catch (IOException e) {
						err.println("Exception while reading arguments of command["
								+ commandName + "].");
						e.printStackTrace();
					}
					if (args != null)
						try {
							String message = c.invoke(args);
							out.println(message);
						} catch (Exception e) {
							err.println("Exception while invoking command["
									+ commandName + "] with arguments [" + args
									+ "].");
							e.printStackTrace();
							out.println("usage: " + commandName + " "
									+ c.getUsage());
						}

				} else {
					out.println("Unregistered command [" + commandName + "].\n");
					printAllUsage();
				}
			}
			out.flush();
			err.flush();
		}
	}

	private List<String> processArguments() throws IOException {
		List<String> result = new ArrayList<String>();

		st.nextToken();
		while (st.ttype != '\n') {
			result.add(st.sval);
			st.nextToken();
		}
		return result;
	}

	private void printAllUsage() {
		out.println(getAllUsage());
	}

	/**
	 * Default command prints out available registered commands.
	 * 
	 * @author tws
	 * 
	 */
	class Help implements Command {

		@Override
		public String getUsage() {
			return "";
		}

		@Override
		public String invoke(List<String> arguments) throws Exception {
			return getAllUsage();
		}
	}

	private String getAllUsage() {
		String result = "Available commands:\n";
		for (String command : commands.keySet())
			result += command + " " + commands.get(command).getUsage() + "\n";
		return result;
	}
}
