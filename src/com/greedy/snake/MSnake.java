package com.greedy.snake;

import javax.swing.JFrame;

public class MSnake {

	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		frame.setSize(916, 738);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(frame);
		
		frame.add(new MPanel());
		
		frame.setVisible(true);
	}

}
