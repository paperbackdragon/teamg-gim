package util;

import java.util.LinkedList;

public class CommandBuffer<T> {

	private LinkedList<T> commands;

	public CommandBuffer() {
		this.commands = new LinkedList<T>();
	}

	public synchronized T getCommand() throws InterruptedException {
		while (commands.size() == 0) {
			wait();
		}
			notifyAll();
			return commands.remove();
	}

	public synchronized void putCommand(T cmd) {
			commands.add(cmd);
			notifyAll();
	}

}
