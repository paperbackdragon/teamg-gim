package client;

import javax.swing.JComponent;

/**
 * A listener for the EditableJLabel. Called when the value of the JLabel is
 * updated.
 * 
 * @author James McMinn
 * 
 */
public interface ValueChangedListener {
	public void valueChanged(String value, JComponent source);
}
