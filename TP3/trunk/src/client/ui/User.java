package client.ui;

public class User {
	
	private String email;
	private String nickname;
	private String status;
	private String personalMessage;
	private String displayPic;

	public User(String email) {
		this.email = email;
		this.nickname = null;
		this.status = null;
		this.personalMessage = null;
		this.displayPic = null;
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
