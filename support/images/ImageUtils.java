/*
 * Copyright 2017 Marc Liberatore.
 */

package images;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageUtils {
	public static List<BufferedImage> loadFromDirectory(File directory) {
		List<BufferedImage> list = new ArrayList<>();
		for (File f: directory.listFiles()) {
			BufferedImage image;
			try {
				image = ImageIO.read(f);
			} catch (IOException e) {
				continue;
			}
			if (image == null) continue;
			list.add(image);
		}
		return list;
	}
	
	public static int hue(int rgb) {
		final ColorModel colorModel = ColorModel.getRGBdefault();
		
		final double DEGREES_TO_RADIANS = (Math.PI / 180.0);
		final int R = colorModel.getRed(rgb);
		final int G = colorModel.getGreen(rgb);
		final int B = colorModel.getBlue(rgb);

		final double alpha = (2.0 * R - G - B) / 2.0 / 255.0;
		final double beta  = (G - B) / (2.0 / Math.sqrt(3.0)) / 255.0;
		double hue = Math.atan2(beta, alpha) / DEGREES_TO_RADIANS;
		if (hue < 0) {
			hue += 360.0;
		}
		hue = hue * 256.0 / 360.0;
		return (int)Math.round(hue);
	}
	
	public static int averageHue(BufferedImage image) {
		int numPixels = 0;
		double totalHue = 0.0;
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				final int pixel = image.getRGB(x, y);
				totalHue += hue(pixel);
				numPixels++;
			}
		}
		return (int)Math.round(totalHue / numPixels);
	}
	
	public static BufferedImage resize(BufferedImage image, int width, int height) { 
	    Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	    BufferedImage targetImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = targetImage.createGraphics();
	    g2d.drawImage(scaledImage, 0, 0, null);
	    g2d.dispose();

	    return targetImage;
	}  

	public static int[] getPixels(BufferedImage image) {
		int[] array = new int[image.getWidth() * image.getHeight()];
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				array[y * image.getWidth() + x] = image.getRGB(x, y);
			}
		}
		return array;
	}
}
