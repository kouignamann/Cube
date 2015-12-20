/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.kouignamann.cube.core.utils;

import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.*;
import org.slf4j.*;

import javax.imageio.*;
import java.awt.*;
import java.awt.color.*;
import java.awt.image.*;
import java.io.*;
import java.nio.*;
import java.util.regex.*;

import static fr.kouignamann.cube.core.Constant.*;

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

	public static void saveBufferedTexture(int colorTextureId) {
		ByteBuffer imgRGBABuffer = BufferUtils.createByteBuffer(SCREEN_WIDTH * SCREEN_HEIGTH * 3);
		GL11.glReadPixels(0, 0, SCREEN_WIDTH, SCREEN_HEIGTH, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, imgRGBABuffer);

		byte[] array = new byte[SCREEN_WIDTH*SCREEN_HEIGTH*3];
		imgRGBABuffer.get(array);
		DataBufferByte dbb = new DataBufferByte(array, imgRGBABuffer.capacity());
		WritableRaster raster = Raster.createInterleavedRaster(
				dbb,
				SCREEN_WIDTH, SCREEN_HEIGTH,
				SCREEN_WIDTH * 3,
				3,
				new int []{ 0, 1, 2 },
				null);

		ColorModel colorModel =  new ComponentColorModel(
				ColorSpace.getInstance(ColorSpace.CS_sRGB),
				new int[] { 8, 8, 8 },
				false,
				false,
				Transparency.OPAQUE,
				DataBuffer.TYPE_BYTE);

		BufferedImage bfImage = new BufferedImage(colorModel, raster, false, null);

		try {
			ImageIO.write(bfImage, "png", new File("d:/output.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
