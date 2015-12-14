/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.kouignamann.cube.core.utils;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.TextureLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.regex.Pattern;

public class TextureUtils {

	private static Logger logger = LoggerFactory.getLogger(TextureUtils.class);
	
	private static final String PNG_FILE_PATTERN = ".*(png|PNG)";
	private static final String PNG_EXT			 = "PNG";
	
	private static final String JPG_FILE_PATTERN = ".*(jpg|JPG)";
	private static final String JPG_EXT			 = "JPG";
	
	private static final String TGA_FILE_PATTERN = ".*(tga|TGA)";
	private static final String TGA_EXT			 = "TGA";

	public static int loadTexture(String fileName) {
		try {
			if (Pattern.matches(PNG_FILE_PATTERN, fileName)) {
				return TextureLoader.getTexture(PNG_EXT, TextureUtils.class.getResourceAsStream(fileName)).getTextureID();
			} else if (Pattern.matches(JPG_FILE_PATTERN, fileName)) {
				return TextureLoader.getTexture(JPG_EXT, TextureUtils.class.getResourceAsStream(fileName)).getTextureID();
			} else if (Pattern.matches(TGA_FILE_PATTERN, fileName)) {
				return TextureLoader.getTexture(TGA_EXT, TextureUtils.class.getResourceAsStream(fileName)).getTextureID();
			}
			logger.error(String.format("Texture file is not an handled format : '%s'", fileName));
			throw new IllegalArgumentException(String.format("Texture file is not an handled format : '%s'", fileName));
		}
		catch (IOException e) {
			logger.error(String.format("Unable to load texture file : '%s'", fileName), e);
			throw new IllegalArgumentException(String.format("Unable to load texture file : '%s'", fileName), e);
		}
	}

	public static void destroyTextures(int[] textureIds) {
		if (textureIds == null) {
			return;
		}
		for (int textureId : textureIds) {
			GL11.glDeleteTextures(textureId);
		}
	}
}
