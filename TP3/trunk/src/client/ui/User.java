package client.ui;

public class User {

	private String nickname;
	private String status;
	private String personalMessage;
	private String displayPic;
	private Object lock;

	public User() {
		this.nickname = null;
		this.status = null;
		this.personalMessage = null;
		this.displayPic = null;
	}

	public String getNickname() {
		synchronized (lock) {
			lock.notifyAll();
			return nickname;
		}

	}

	public void setNickname(String nickname) {
		synchronized (lock) {
			lock.notifyAll();
			this.nickname = nickname;
		}
	}

	public String getStatus() {
		synchronized (lock) {
			lock.notifyAll();
			return status;
		}
	}

	public void setStatus(String status) {
		synchronized (lock) {
			lock.notifyAll();
			this.status = status;
		}
	}

	public String getPersonalMessage() {
		synchronized (lock) {
			lock.notifyAll();
			return personalMessage;
		}
	}

	public void setPersonalMessage(String personalMessage) {
		synchronized (lock) {
			lock.notifyAll();
			this.personalMessage = personalMessage;
		}
	}

	public String getDisplayPic() {
		synchronized (lock) {
			lock.notifyAll();
			return displayPic;
		}
	}

	public void setDisplayPic(String displayPic) {
		synchronized (lock) {
			lock.notifyAll();
			this.displayPic = displayPic;
		}
	}

}
