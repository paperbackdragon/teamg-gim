package util;

import java.util.LinkedList;

public class CommandBuffer<T> {

	private LinkedList<T> commands;
	private LinkedList<T> responses;

	public CommandBuffer() {
		this.commands = new LinkedList<T>();
		this.responses = new LinkedList<T>();
	}

	public synchronized T getCommand() {
		while (commands.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// We're screwed
			}
		}
		notifyAll();
		return commands.remove();
	}

	public synchronized void putCommand(T cmd) {
		commands.add(cmd);
		notifyAll();
	}

	public synchronized T getResponse() {
		while (responses.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// We're screwed
			}
		}
		notifyAll();
		return responses.remove();
	}

	public synchronized void putResponse(T rsp) {
		responses.add(rsp);
		notifyAll();
	}

}
