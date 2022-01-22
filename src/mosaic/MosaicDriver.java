/*
 * Copyright 2017 Marc Liberatore.
 */

package mosaic;

import images.ImagePanel;
import images.ImageUtils;
import images.Mosaic;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("serial")
public class MosaicDriver extends JFrame {
	private static final int WIDTH = 10;
	private static final int HEIGHT = 10;
	private static final String RESOURCE_DIR = "test" + File.separator + "resources" + File.separator;
	
	public MosaicDriver() throws IOException {
		add(new ImagePanel(makeMosaic()));
		pack();
		setTitle("Image Mosaic");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	public static BufferedImage makeMosaic() throws IOException {
		// first, choose the set of colors (the "palette") for the mosaic
		int[] palette = {0xFF0000, 0x00FF00, 0x0000FF};
		
		// then instantiate a TileFactor with this palette
		TileFactory tileFactory = new TileFactory(palette, WIDTH, HEIGHT);
		
		// load the test images
		for (String color: new String[] {"red", "green", "blue"}) {
			for (BufferedImage image: ImageUtils.loadFromDirectory(new File(RESOURCE_DIR + color))) {				
				tileFactory.addImage(image);
			}
		}
		
		// load the target image
		File file = new File(RESOURCE_DIR + "nature.jpg");
		BufferedImage image = ImageIO.read(file);
		image = ImageUtils.resize(image, 1152, 720);
		
		// create a Mosaic with this target image and the previously initialized TileFactory
		Mosaic mosaic = new Mosaic(image, tileFactory);
		BufferedImage imageMosaic = mosaic.buildMosaic();
		return imageMosaic;
	}
	
	public static void main(String[] args) throws IOException {
		EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                MosaicDriver m;
				try {
					m = new MosaicDriver();
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
                m.setVisible(true);
            }
        });
	}
}
