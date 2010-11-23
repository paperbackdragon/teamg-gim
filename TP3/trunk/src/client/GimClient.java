package client;

import java.awt.*;

import javax.swing.*;

public class GimClient extends JFrame{
	public GimClient() {
		setTitle("GIM");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel main = new MainWindow();
		
		setContentPane(main);
		pack();
		setVisible(true);
	}
	
	class MainWindow extends JPanel {
		public MainWindow() {
			
		}
		
		public Dimension getPreferredSize() {
			return new Dimension(300, 400);
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GimClient(); 
			}
		});
	}
}
