/*
 * Copyright 2017 Marc Liberatore.
 */

package images;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {	
	private final BufferedImage image;

	public ImagePanel(BufferedImage image) {
		this.image = image;
		setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, null);
	}
}
