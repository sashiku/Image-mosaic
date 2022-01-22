/*
 * Copyright 2017 Marc Liberatore.
 */

package images;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import mosaic.TileFactory;

public class Mosaic {
	private final BufferedImage image;
	private final TileFactory tileFactory;

	public Mosaic(BufferedImage image, TileFactory tileFactory) {
		this.image = image;
		this.tileFactory = tileFactory;
	}
			
	/**
	 * Constructs and returns the mosaic.
	 * 
	 * The mosaic always consists of an integer number of tiles; if the target
	 * image is not exactly as wide or as high as a multiple of the tile width/height,
	 * the mosaic is truncated to be exactly a multiple of the tile width/height.
	 * 
	 * @return the mosaic
	 */
	public BufferedImage buildMosaic() {
		final int tileWidth = tileFactory.tileWidth;
		final int tileHeight = tileFactory.tileHeight;

		final int tilesWide = image.getWidth() / tileWidth;
		final int tilesHigh = image.getHeight() / tileHeight;
		BufferedImage mosaic = new BufferedImage(tilesWide * tileWidth, tilesHigh * tileHeight, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = mosaic.createGraphics();
		
		for (int i = 0; i < tilesWide; i++) {
			for (int j = 0; j < tilesHigh; j++) {
				final int x = i * tileWidth;
				final int y = j * tileHeight;
				BufferedImage tile = image.getSubimage(x, y, tileWidth, tileHeight);
				int averageHue = ImageUtils.averageHue(tile);
				g2d.drawImage(tileFactory.getTile(averageHue), x, y, null);
			}
		}
		g2d.dispose();
		return mosaic;
	}
}
