package client.ui;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import client.Model;

public class OptionsPanel extends JPanel{
	
	// feel free to make this prettier, i'm just doing it to test the backend :P
	
	private static final long serialVersionUID = 1L;
	private static Model model = Model.getInstance();
	
	private String personalMessage;
	private String displayPicture;
	private String nickname;
	
	private JTextArea nicknametext;
	private JTextArea personalmessagetext;
	private JComboBox statusbox;
	
	public OptionsPanel() {
		this.personalMessage = model.getOwnPersonalMessage();
		this.nickname = model.getOwnNickname();
		
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
