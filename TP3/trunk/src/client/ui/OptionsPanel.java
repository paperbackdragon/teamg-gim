package client.ui;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import client.GimClient;

public class OptionsPanel extends JPanel{
	
	// feel free to make this prettier, i'm just doing it to test the backend :P
	
	private static final long serialVersionUID = 7546695950230803593L;
	private String personalMessage;
	private String status;
	private String displayPicture;
	private String nickname;
	
	private JTextArea nicknametext;
	private JTextArea personalmessagetext;
	private JComboBox statusbox;
	
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
		String[] options = {"Online", "Busy", "Away", "Offline"};
		statusbox = new JComboBox(options);
		add(statusbox);
		
	}
	
	
	// these are needed for extracting this info...
	
	
	public String getPersonalMessageText() {
		return personalmessagetext.getText();
	}

	public String getStatusText() {
		return (String) statusbox.getSelectedItem();
	}

	public String getDisplayPictureText() {
		return displayPicture;
	}


	public String getNicknameText() {
		return nicknametext.getText();
	}
	

}
