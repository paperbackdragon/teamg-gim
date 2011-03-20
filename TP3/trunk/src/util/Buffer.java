package util;

import java.util.LinkedList;

public class Buffer<T> {

	private LinkedList<T> commands;

	public Buffer() {
		this.commands = new LinkedList<T>();
	}

	public synchronized T getCommand() {
		while (commands.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		
		T cmd = commands.remove();
		notifyAll();
		
		return cmd;
	}

	public synchronized void putCommand(T cmd) {
		commands.add(cmd);
		notifyAll();
	}

	public boolean isEmpty() {
		return (commands.size() == 0);
	}

}
