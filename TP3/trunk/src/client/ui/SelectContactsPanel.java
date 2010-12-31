package client.ui;

import java.util.ArrayList;

import javax.swing.JCheckBox;

public class SelectContactsPanel {
	
	ArrayList<JCheckBox> boxes;
	
	public SelectContactsPanel(String[] friendlist) {
		
		boxes = new ArrayList<JCheckBox>();
		
		for (int i = 0; i < friendlist.length; i++) {
			boxes.add(new JCheckBox(friendlist[i]));
			boxes.get(i).setVisible(true);
		}
	
	}
	
	public ArrayList<JCheckBox> getBoxes() {
		return boxes;
	}

}
