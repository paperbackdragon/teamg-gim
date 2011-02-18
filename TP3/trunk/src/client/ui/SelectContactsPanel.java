package client.ui;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import client.User;

public class SelectContactsPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	ArrayList<JCheckBox> boxes;

	String[] roomparticipants;

	public SelectContactsPanel(Collection<User> friendlist, String[] participants) {

		boxes = new ArrayList<JCheckBox>();
		roomparticipants = participants;

		for (User u : friendlist) {
			JCheckBox checkbox = new JCheckBox(u.getEmail());

			if (inroomparticipants(u.getEmail()))
				checkbox.setEnabled(false);

			checkbox.setVisible(true);
			add(checkbox);

			boxes.add(checkbox);
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
