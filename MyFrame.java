package pongGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MyFrame extends JFrame {
	MyPanel gamePanel;
	public MyFrame() {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle("Pong");
		this.setResizable(false);
		gamePanel=new MyPanel();
		this.add(gamePanel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

}
