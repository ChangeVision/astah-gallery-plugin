package com.change_vision.astah.internal.view;

import javax.swing.JFrame;

public class GalleryViewTest {

	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Gallery");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 500);
		frame.setLocationRelativeTo(null);
		frame.add(new GalleryView());
		frame.setVisible(true);
		
	}

}
