package client.ui;

import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class SelectContactsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	ArrayList<JCheckBox> boxes;
	String[] roomparticipants;

	public SelectContactsPanel(String[] friendlist, String[] participants) {

		boxes = new ArrayList<JCheckBox>();
		roomparticipants = participants;

		for (int i = 0; i < friendlist.length; i++) {

			boxes.add(new JCheckBox(friendlist[i]));
			add(boxes.get(i));
			
			if (inroomparticipants(friendlist[i])) {
				boxes.get(i).setEnabled(false);
			}
			
			boxes.get(i).setVisible(true);
		}
		this.setVisible(true);

	}

	private boolean inroomparticipants(String tocheck) {

		for (int i = 0; i < roomparticipants.length; i++) {
			if (roomparticipants[i].equals(tocheck)) {
				return true;
			}
		}

		return false;
	}

	public ArrayList<JCheckBox> getBoxes() {
		return boxes;
	}

}
