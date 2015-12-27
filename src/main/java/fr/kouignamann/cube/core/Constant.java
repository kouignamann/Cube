package fr.kouignamann.cube.core;

import org.lwjgl.util.vector.*;

public final class Constant {

	// SCREEN
	public final static String		WINDOW_NAME						= "Cube App";
	public final static int			SCREEN_WIDTH					= 1024;
	public final static int			SCREEN_HEIGHT					= 768;

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
	public final static long		CLICK_MS_COOLDOWN   			= 500;
	public final static long		LISTENER_THREADS_MS_COOLDOWN	= 20;
	public final static float		WHEEL_SENSITIVITY   			= 5.0f;
	public final static float		MOUSE_SENSITIVITY   			= 20.0f;

	// CAMERA
	public final static Vector3f	INITIAL_CAMERA_POSITION			= new Vector3f(0, 0, -1);
	public final static Vector3f	INITIAL_CAMERA_ROTATION			= new Vector3f(0, 0, 0);

	// GRAPHICS
	public final static int			SCALE							= 500;

	// LOGICS
	public final static float		CUBE_SCALE_SPEED				= 1.0f;
	public final static float		CUBE_ROTATION_SPEED				= 1.0f;
}
