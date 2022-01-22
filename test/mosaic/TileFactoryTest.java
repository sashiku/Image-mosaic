/*
 * Copyright 2017 Marc Liberatore.
 */

package mosaic;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;


import images.ImageUtils;

public class TileFactoryTest {
//	@Rule
//	public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds

	private TileFactory redOnlyTileFactory;
	private TileFactory threeColorTileFactory;
	private int hueRed;
	private int hueGreen;
	private int hueBlue;
	
	private static final int WIDTH = 10;
	private static final int HEIGHT = 10;
	private static final int RGB_RED = 0xFF0000;
	private static final int RGB_GREEN = 0x00FF00;
	private static final int RGB_BLUE = 0x0000FF;
	private static final String RESOURCE_DIR = "test" + File.separator + "resources" + File.separator;

	@Before
	public void setup() throws InterruptedException {
		
		hueRed = Math.round(ImageUtils.hue(RGB_RED));
		hueGreen = Math.round(ImageUtils.hue(RGB_GREEN));
		hueBlue = Math.round(ImageUtils.hue(RGB_BLUE));
		
		redOnlyTileFactory = new TileFactory(new int[] {RGB_RED}, WIDTH, HEIGHT);
		
		int[] palette = {RGB_RED, RGB_GREEN, RGB_BLUE};
		threeColorTileFactory = new TileFactory(palette, WIDTH, HEIGHT);
	}

	@Test
	public void testSimpleHueDistance() {
		assertEquals(10, TileFactory.hueDistance(10, 20));
	}

	@Test
	public void testHueDistanceWithZeroPositive() {
		assertEquals(10, TileFactory.hueDistance(0, 10));
	}

	@Test
	public void testHueDistanceWithZeroNegative() {
		assertEquals(10, TileFactory.hueDistance(0, 246));
	}

	@Test
	public void testHueDistanceAcrossZero() {
		assertEquals(20, TileFactory.hueDistance(10, 246));
	}

	@Test
	public void testHueDistanceHalfway() {
		assertEquals(128, TileFactory.hueDistance(0, 128));
	}

	@Test
	public void testHueDistanceHalfway2() {
		assertEquals(128, TileFactory.hueDistance(192, 64));
	}

	@Test
	public void testClosestHueExact() {
		assertEquals(0, (int)redOnlyTileFactory.closestHue(0xFF0000));
	}
	
	@Test
	public void testClosestHueInexact() {
		assertEquals(0, (int)redOnlyTileFactory.closestHue(0xFF1414));
	}
	
	@Test
	public void testClosestHue3Exact1() {
		assertEquals(hueRed, (int)threeColorTileFactory.closestHue(hueRed));
	}

	@Test
	public void testClosestHue3Exact2() {
		assertEquals(hueGreen, (int)threeColorTileFactory.closestHue(hueGreen));
	}
	
	@Test
	public void testClosestHue3Exact3() {
		assertEquals(hueBlue, (int)threeColorTileFactory.closestHue(hueBlue));
	}

	@Test
	public void testClosestHue3Inexact() {
		assertEquals(hueRed, (int)threeColorTileFactory.closestHue(240));
	}

	@Test
	public void testClosestHue3Inexact2() {
		assertEquals(hueGreen, (int)threeColorTileFactory.closestHue(43));
	}

	@Test
	public void testClosestHue3Inexact3() {
		assertEquals(hueBlue, (int)threeColorTileFactory.closestHue(180));
	}

	@Test
	public void testGetOneTile() {
		List<BufferedImage> redImages = ImageUtils.loadFromDirectory(new File(RESOURCE_DIR + "red")); 
		redOnlyTileFactory.addImage(redImages.get(0));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(0), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(redOnlyTileFactory.getTile(hueRed)));
	}

	@Test
	public void testGetOneTileInexact() {
		List<BufferedImage> redImages = ImageUtils.loadFromDirectory(new File(RESOURCE_DIR + "red")); 
		redOnlyTileFactory.addImage(redImages.get(0));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(0), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(redOnlyTileFactory.getTile(hueRed + 1)));
	}

	@Test
	public void testGetMultipleSingleTiles() {
		List<BufferedImage> redImages = ImageUtils.loadFromDirectory(new File(RESOURCE_DIR + "red")); 
		redOnlyTileFactory.addImage(redImages.get(0));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(0), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(redOnlyTileFactory.getTile(hueRed)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(0), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(redOnlyTileFactory.getTile(hueRed)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(0), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(redOnlyTileFactory.getTile(hueRed)));
	}

	@Test
	public void testGetMultipleTiles() {
		List<BufferedImage> redImages = ImageUtils.loadFromDirectory(new File(RESOURCE_DIR + "red"));
		for (BufferedImage tile: redImages) {
			redOnlyTileFactory.addImage(tile);
		}
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(0), WIDTH, HEIGHT)),
				ImageUtils.getPixels(redOnlyTileFactory.getTile(hueRed)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(1), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(redOnlyTileFactory.getTile(hueRed)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(2), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(redOnlyTileFactory.getTile(hueRed)));
	}

	@Test
	public void testGetMultipleTilesWrap() {
		List<BufferedImage> redImages = ImageUtils.loadFromDirectory(new File(RESOURCE_DIR + "red"));
		for (BufferedImage tile: redImages) {
			redOnlyTileFactory.addImage(tile);
		}
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(0), WIDTH, HEIGHT)),
				ImageUtils.getPixels(redOnlyTileFactory.getTile(hueRed)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(1), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(redOnlyTileFactory.getTile(hueRed)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(2), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(redOnlyTileFactory.getTile(hueRed)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(0), WIDTH, HEIGHT)),
				ImageUtils.getPixels(redOnlyTileFactory.getTile(hueRed)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(1), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(redOnlyTileFactory.getTile(hueRed)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(2), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(redOnlyTileFactory.getTile(hueRed)));
	}

	@Test
	public void testGetMultipleTilesMultipleColors() {
		List<BufferedImage> redImages = ImageUtils.loadFromDirectory(new File(RESOURCE_DIR + "red"));
		List<BufferedImage> greenImages = ImageUtils.loadFromDirectory(new File(RESOURCE_DIR + "green"));
		List<BufferedImage> blueImages = ImageUtils.loadFromDirectory(new File(RESOURCE_DIR + "blue"));
		for (BufferedImage tile: redImages) {
			threeColorTileFactory.addImage(tile);
		}
		for (BufferedImage tile: greenImages) {
			threeColorTileFactory.addImage(tile);
		}
		for (BufferedImage tile: blueImages) {
			threeColorTileFactory.addImage(tile);
		}
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(0), WIDTH, HEIGHT)),
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueRed)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(1), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueRed)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(2), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueRed)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(greenImages.get(0), WIDTH, HEIGHT)),
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueGreen)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(greenImages.get(1), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueGreen)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(greenImages.get(2), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueGreen)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(blueImages.get(0), WIDTH, HEIGHT)),
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueBlue)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(blueImages.get(1), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueBlue)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(blueImages.get(2), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueBlue)));
	}

	@Test
	public void testGetMultipleTilesMultipleColorsWrap() {
		List<BufferedImage> redImages = ImageUtils.loadFromDirectory(new File(RESOURCE_DIR + "red"));
		List<BufferedImage> greenImages = ImageUtils.loadFromDirectory(new File(RESOURCE_DIR + "green"));
		List<BufferedImage> blueImages = ImageUtils.loadFromDirectory(new File(RESOURCE_DIR + "blue"));
		for (BufferedImage tile: redImages) {
			threeColorTileFactory.addImage(tile);
		}
		for (BufferedImage tile: greenImages) {
			threeColorTileFactory.addImage(tile);
		}
		for (BufferedImage tile: blueImages) {
			threeColorTileFactory.addImage(tile);
		}
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(0), WIDTH, HEIGHT)),
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueRed)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(1), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueRed)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(2), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueRed)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(greenImages.get(0), WIDTH, HEIGHT)),
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueGreen)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(greenImages.get(1), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueGreen)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(greenImages.get(2), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueGreen)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(blueImages.get(0), WIDTH, HEIGHT)),
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueBlue)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(blueImages.get(1), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueBlue)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(blueImages.get(2), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueBlue)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(0), WIDTH, HEIGHT)),
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueRed)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(1), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueRed)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(redImages.get(2), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueRed)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(greenImages.get(0), WIDTH, HEIGHT)),
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueGreen)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(greenImages.get(1), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueGreen)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(greenImages.get(2), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueGreen)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(blueImages.get(0), WIDTH, HEIGHT)),
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueBlue)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(blueImages.get(1), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueBlue)));
		assertArrayEquals(ImageUtils.getPixels(ImageUtils.resize(blueImages.get(2), WIDTH, HEIGHT)), 
				ImageUtils.getPixels(threeColorTileFactory.getTile(hueBlue)));

	}
}
