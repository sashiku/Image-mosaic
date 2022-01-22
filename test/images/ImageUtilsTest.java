/*
 * Copyright 2017 Marc Liberatore.
 */

package images;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;



public class ImageUtilsTest {
//	@Rule
//	public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds

	private static final String RESOURCE_DIR = "test" + File.separator + "resources" + File.separator;
	
		
	@Test
	public void testLoadTwo() {
		assertEquals(2, ImageUtils.loadFromDirectory(new File(RESOURCE_DIR)).size());
	}

	@Test
	public void testLoadThree() {
		assertEquals(3, ImageUtils.loadFromDirectory(new File(RESOURCE_DIR + "green")).size());
	}
	
	@Test
	public void testHueRed() {
		assertEquals(0, ImageUtils.hue(0xFF0000));
	}

	@Test
	public void testHueGreen() {
		assertEquals((int)Math.round(120. * 256. / 360.), ImageUtils.hue(0x00FF00));
	}


	@Test
	public void testHueBlue() {
		assertEquals((int)Math.round(240. * 256. / 360.), ImageUtils.hue(0x0000FF));
	}
}
