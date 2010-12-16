package util;

import java.util.LinkedList;

public class CommandBuffer<T> {

	private LinkedList<T> commands;
	private LinkedList<T> responses;

	public CommandBuffer() {
		this.commands = new LinkedList<T>();
		this.responses = new LinkedList<T>();
	}

	public synchronized T getCommand() throws InterruptedException {
		synchronized (commands) {
			while (commands.size() == 0) {
				wait();
			}
			notifyAll();
			return commands.remove();
		}
	}

	public void putCommand(T cmd) {
		synchronized (commands) {
			commands.add(cmd);
			notifyAll();
		}
	}

	public T getResponse() throws InterruptedException {
		synchronized (responses) {
			while (responses.size() == 0) {
				wait();
			}
			notifyAll();
			return responses.remove();
		}
	}

	public void putResponse(T rsp) {
		synchronized (responses) {
			responses.add(rsp);
			notifyAll();
		}
	}

}
