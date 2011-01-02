package client.ui;

import javax.swing.JPanel;

import client.GimClient;

public class OptionsPanel extends JPanel{
	
	// feel free to make this prettier, i'm just doing it to test the backend :P
	
	private String personalMessage;
	private String status;
	private String displayPicture;
	private String nickname;
	
	public OptionsPanel() {
		this.personalMessage = GimClient.getClient().getOwnPersonalMessage();
		this.status = GimClient.getClient().getOwnStatus();
		this.nickname = GimClient.getClient().getOwnNickname();
	}

}
