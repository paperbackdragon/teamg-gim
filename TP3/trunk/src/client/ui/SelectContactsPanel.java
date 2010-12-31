package client.ui;

import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class SelectContactsPanel extends JPanel {
	
	ArrayList<JCheckBox> boxes;
	
	public SelectContactsPanel(String[] friendlist) {
		
		boxes = new ArrayList<JCheckBox>();
		
		for (int i = 0; i < friendlist.length; i++) {
			boxes.add(new JCheckBox(friendlist[i]));
			add(boxes.get(i));
			boxes.get(i).setVisible(true);
		}
		this.setVisible(true);
	
	}
	
	public ArrayList<JCheckBox> getBoxes() {
		return boxes;
	}

}
