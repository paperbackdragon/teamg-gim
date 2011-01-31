package client.ui;

import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.ValueChangedListener;

/**
 * Create a JPanel with a CardLayout which switches to another JPanel on hover
 * 
 * @author James McMinn
 * 
 */
public class ListJLabel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel label;
	private JComboBox combo;
	private LinkedList<ValueChangedListener> listeners = new LinkedList<ValueChangedListener>();

	/**
	 * Create the new panel
	 * 
	 * @param startText
	 *            The starting text
	 */
	ListJLabel(String[] options, int selected) {
		super();

		// Create the listener and the layout
		CardLayout layout = new CardLayout(0, 0);
		this.setLayout(layout);
		EditableListener hl = new EditableListener();

		// Create the JPanel for the "normal" state
		JPanel labelPanel = new JPanel(new GridLayout(1, 1));
		label = new JLabel(options[selected]);

		labelPanel.add(label);

		// Create the JPanel for the "hover state"
		JPanel inputPanel = new JPanel(new GridLayout(1, 1));

		combo = new JComboBox(options);
		combo.setSelectedIndex(selected);

		combo.addActionListener(hl);
		combo.addMouseListener(hl);
		combo.addKeyListener(hl);
		combo.addFocusListener(hl);

		label.setBorder(new JTextField().getBorder());
		
		inputPanel.add(combo);

		this.addMouseListener(hl);

		// Set the states
		this.add(labelPanel, "NORMAL");
		this.add(inputPanel, "HOVER");

		// Show the correct panel to begin with
		layout.show(this, "NORMAL");
	}

	/**
	 * Get the text from the label
	 * 
	 * @return The text from the label
	 */
	public String getValue() {
		return this.label.getText();
	}

	public void setValue(int index) {
		this.combo.setSelectedIndex(index);
		this.label.setText((String) this.combo.getItemAt(index));
	}

	/**
	 * Get the label
	 * 
	 * @return the label
	 */
	public JLabel getLabel() {
		return label;
	}

	/**
	 * Set the hover state of the Panel
	 * 
	 * @param hover
	 *            True will set the state to hovering and show the input box.
	 *            False will show the label.
	 */
	public void setHoverState(boolean hover) {
		CardLayout cl = (CardLayout) (this.getLayout());

		if (hover)
			cl.show(this, "HOVER");
		else
			cl.show(this, "NORMAL");
	}

	/**
	 * Add a value changed listener to this EditableJLabel
	 * 
	 * @param valueListener
	 */
	public void addValueChangedListener(ValueChangedListener valueListener) {
		this.listeners.add(valueListener);
	}

	/**
	 * Listen for nearly everything happening
	 */
	public class EditableListener implements ActionListener, MouseListener, KeyListener, FocusListener {

		boolean locked = false;
		int oldValue;

		/**
		 * Lock to the text field while we have focus
		 */
		@Override
		public void focusGained(FocusEvent arg0) {
			oldValue = combo.getSelectedIndex();
		}

		/**
		 * Release the lock so that we can go back to a JLabel
		 */
		public void release() {
			this.locked = false;
		}

		/**
		 * Check for mouse over
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
			setHoverState(true);
		}

		/**
		 * Check for the mouse exiting and set the sate back to normal if
		 * possible
		 */
		@Override
		public void mouseExited(MouseEvent e) {
			if (!locked && !combo.contains(e.getPoint()))
				setHoverState(false);
		}

		/**
		 * Update the text when focus is lost and release the lock
		 */
		@Override
		public void focusLost(FocusEvent e) {
			setValue(combo.getSelectedIndex());
			release();
			setHoverState(false);
		}

		/**
		 * Check for key presses. We're only interested in Enter (save the value
		 * of the field) and Escape (reset the field to its previous value)
		 */
		@Override
		public void keyTyped(KeyEvent e) {
			if (e.getKeyChar() == KeyEvent.VK_ENTER) {
				if (oldValue != combo.getSelectedIndex()) {
					setValue(combo.getSelectedIndex());
					for (ValueChangedListener v : listeners) {
						v.valueChanged((String) combo.getSelectedItem(), ListJLabel.this);
					}
				}
				release();
				setHoverState(false);
			} else if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
				release();
				setHoverState(false);
				setValue(oldValue);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (!locked)
				locked = true;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equalsIgnoreCase("comboBoxChanged")) {
				if (oldValue != combo.getSelectedIndex()) {
					setValue(combo.getSelectedIndex());
					for (ValueChangedListener v : listeners) {
						v.valueChanged((String) combo.getSelectedItem(), ListJLabel.this);
					}
				}
				release();
				setHoverState(false);
			}
		}

	}

}
