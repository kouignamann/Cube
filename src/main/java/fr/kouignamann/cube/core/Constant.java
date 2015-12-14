package fr.kouignamann.cube.core;

public final class Constant {

	// SCREEN
	public final static String		WINDOW_NAME						= "Cube App";
	public final static int			SCREEN_WIDTH					= 640;
	public final static int			SCREEN_HEIGTH					= 640;

	// COLORS
	public final static float[]		BLACK							= new float[]{0f, 0f, 0f, 1f};
	public final static float[]		WHITE							= new float[]{1f, 1f, 1f, 1f};
	public final static float[]		RED								= new float[]{1f, 0f, 0f, 1f};
	public final static float[]		BLUE							= new float[]{0f, 0f, 1f, 1f};
	public final static float[]		ORANGE							= new float[]{1f, 0.6f, 0.2f, 1f};
	public final static float[]		GREEN							= new float[]{0f, 1f, 0f, 1f};
	public final static float[]		YELLOW							= new float[]{1f, 1f, 0f, 1f};
	public final static float[]		TRANSPARENT						= new float[]{1f, 1f, 1f, 0f};

	// LISTENERS
	public final static long		KEY_HIT_MS_COOLDOWN				= 150;
	public final static long		LISTENER_THREADS_COOLDOWN		= 20;
}
