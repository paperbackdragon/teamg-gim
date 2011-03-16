package server;

public class Timeout implements Runnable {

	private Data data = Data.getInstance();
	private long timeoutTime;
	private long startup;

	/**
	 * Constructor
	 * 
	 * @param time
	 *            The time in seconds which a worker has till it timeouts
	 */
	public Timeout(long time) {
		this.timeoutTime = time;
		this.startup = System.currentTimeMillis();
	}

	public String uptime() {
		int seconds = (int) ((System.currentTimeMillis() - this.startup) / 1000);
		int minutes = seconds / 60;
		int hours = minutes / 60;
		int days = hours / 24;
		seconds = seconds % 60;
		minutes = minutes % 60;
		hours = hours % 24;
		return String.format("%d days %02d:%02d:%02d", days, hours, minutes, seconds);
	}

	@Override
	public void run() {
		while (true) {

			try {
				Thread.sleep((long) timeoutTime * 1000);
			} catch (InterruptedException e) {
				continue;
			}

			int online = 0;

			for (Worker w : data.getWorkers()) {
				if (w.getLastCommunicationTimeDifference() > this.timeoutTime)
					w.quit();
				else if (w.isLoggedin())
					online++;
			}

			data.uptime = uptime();
			data.online = online;

		}
	}

}
