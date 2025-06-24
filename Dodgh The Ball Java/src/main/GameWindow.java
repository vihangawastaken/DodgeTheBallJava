package main;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow{
	private JFrame jframe;
	public GameWindow() {
		jframe = new JFrame();
		
		jframe.setSize(800, 600);		
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GamePanel panel = new GamePanel();
		
		jframe.add(panel);
		jframe.setResizable(false);
		jframe.setVisible(true);
	}
}
