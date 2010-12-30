package client.ui;

public class User {

	// gordon: THREAD SAFETY? (had attempted to implement it, but think
	// i did it wrong
	
	private String email;
	private String nickname;
	private String status;
	private String personalMessage;
	private String displayPic;
	//private Object lock;

	public User() {
		this.email = null;
		this.nickname = null;
		this.status = null;
		this.personalMessage = null;
		this.displayPic = null;
		//lock = new Object();
	}

	public String getEmail() {
		return email;
	}
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPersonalMessage() {
		return personalMessage;
	}

	public void setPersonalMessage(String personalMessage) {
		this.personalMessage = personalMessage;
	}

	public String getDisplayPic() {
		return displayPic;
	}

	public void setDisplayPic(String displayPic) {
		this.displayPic = displayPic;
	}
}
