package client.ui;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import client.GimClient;

public class OptionsPanel extends JPanel{
	
	// feel free to make this prettier, i'm just doing it to test the backend :P
	
	private String personalMessage;
	private String status;
	private String displayPicture;
	private String nickname;
	
	private JTextArea nicknametext;
	private JTextArea personalmessagetext;
	private JTextArea statustext;
	
	public OptionsPanel() {
		this.personalMessage = GimClient.getClient().getOwnPersonalMessage();
		this.status = GimClient.getClient().getOwnStatus();
		this.nickname = GimClient.getClient().getOwnNickname();
		
		draw();
	}
	
	public void draw() {
		
		setLayout(new GridLayout(7,1));
		
		add(new JLabel("NickName"));
		nicknametext = new JTextArea(nickname);
		add(nicknametext);
		
		add(new JLabel("Personal Message"));
		personalmessagetext = new JTextArea(personalMessage);
		add(personalmessagetext);
		
		add(new JLabel("Status"));
		statustext = new JTextArea(status);
		add(statustext);
		
	}
	
	
	// these are needed for extracting this info...
	
	
	public String getPersonalMessageText() {
		return personalmessagetext.getText();
	}

	public String getStatusText() {
		return statustext.getText();
	}

	public String getDisplayPictureText() {
		return displayPicture;
	}


	public String getNicknameText() {
		return nicknametext.getText();
	}
	

}
