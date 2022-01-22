/*
 * Copyright 2017 Marc Liberatore.
 */

package mosaic;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import images.ImageUtils;

public class TileFactory {
	public final int tileWidth;
	public final int tileHeight;
	// TODO: you will NOT be keeping this array in your final code; 
	// see assignment description for details
	private final Set<Integer> hueSet;
	public Map<Integer, List<BufferedImage>> tileMap = new HashMap<Integer, List<BufferedImage>>();
	public List<BufferedImage> emptyList = new ArrayList<BufferedImage>();
	/**
	 * 
	 * @param colors the palette of RGB colors for this TileFactory
	 * @param tileWidth width (in pixels) for each tile
	 * @param tileHeight height (in pixels) for each tile
	 */
	public TileFactory(int[] colors, int tileWidth, int tileHeight) {
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		hueSet = new HashSet<Integer>();
		//  !!done!! TODO: when you replace the int[] hues, be sure to replace this code, as well
		for (int i = 0; i < colors.length; i++) {
			hueSet.add(Math.round(ImageUtils.hue(colors[i])));
		}
		for (Integer i : hueSet) {
			tileMap.put(i, emptyList);
		}
	}
	
	/**
	 * Returns the distance between two hues on the circle [0,256).
	 * 
	 * @param hue1 
	 * @param hue2
	 * @return the distance between two hues.
	 */
	static int hueDistance(int hue1, int hue2) {

		int hueDistance = 0;
		int inCircleDif;
		int outCircleDif;
		
		if (hue1 > hue2) {
			inCircleDif = hue1 - hue2;
			outCircleDif = (256 - hue1) + hue2;
			hueDistance = Math.min(inCircleDif, outCircleDif);
		}
		if (hue2 > hue1) {
			inCircleDif = hue2 - hue1;
			outCircleDif = (256 - hue2) + hue1;
			hueDistance = Math.min(inCircleDif, outCircleDif);
		}
		if (hue1 == hue2) {
			hueDistance = 0;
		}
		
		return hueDistance;
	}
	
	/**
	 * Returns the closest hue from the fixed palette this TileFactory contains.
	 * @param hue
	 * @return the closest hue from the palette
	 */
	Integer closestHue(int hue) {
		//compare this hue with the hues in hueSet and find the closest one and return in
		int currentHueDistance;
		int smallestHueDistance = 255;
		int closestHue = hue;
		for (Integer i : hueSet) {
			currentHueDistance = hueDistance(hue, i);
			if (currentHueDistance < smallestHueDistance) {
				smallestHueDistance = currentHueDistance;
				closestHue = i;
			}
		}
		
		return closestHue;
	}
	
	/**
	 * Adds an image to this TileFactory for later use.
	 * @param image the image to add
	 */
	public void addImage(BufferedImage image) {
		image = ImageUtils.resize(image, tileWidth, tileHeight);
				
		int avgHue = ImageUtils.averageHue(image);
		int hueKey = closestHue(avgHue);
		List<BufferedImage> temp = new ArrayList<BufferedImage>();
		for (BufferedImage b : tileMap.get(hueKey)) {
			temp.add(b);
		}
		temp.add(image);
		tileMap.put(hueKey, temp);
		
		
		
		// TODO: add the image to the appropriate place in your map from hues to lists of images
	}
	
	/**
	 * Returns the next tile from the list associated with the hue most closely matching the input hue.
	 * 
	 * The returned values should cycle through the list. Each time this method is called, the next 
	 * tile in the list will be returned; when the end of the list is reached, the cycle starts over.
	 *  
	 * @param hue the color to match
	 * @return a tile matching hue
	 */
	public BufferedImage getTile(int hue) {
		// TODO:  return an appropriate image from your map of hues to lists of images; 
		int hueKey = closestHue(hue);
		List<BufferedImage> tileImageList = tileMap.get(hueKey);
		BufferedImage returnTile = tileImageList.get(0);
		Collections.rotate(tileImageList, 1);
		tileMap.put(hueKey, tileImageList);
		return returnTile;
	}
}
