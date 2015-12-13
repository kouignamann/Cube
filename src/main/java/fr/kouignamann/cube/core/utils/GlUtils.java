package fr.kouignamann.cube.core.utils;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class GlUtils {

    private static Logger logger = LoggerFactory.getLogger(GlUtils.class);

    public static void initLwjglLibs() {
        try {
            logger.info("Loading natives");
            java.awt.Toolkit.getDefaultToolkit();
            String libPath = System.getProperty("java.library.path");
            char libPathSeparator = libPath.contains(";") ? ';' : ':';
            System.setProperty(
                    "java.library.path",
                    libPath + libPathSeparator + "target/executable/natives/" + libPathSeparator + "natives/");
            Field sysPath = ClassLoader.class.getDeclaredField("sys_paths");
            sysPath.setAccessible(true);
            sysPath.set(null, null);
            System.loadLibrary("lwjgl64");
        }
        catch (NoSuchFieldException | IllegalAccessException | UnsatisfiedLinkError e) {
            logger.error("Loading natives error", e);
            System.exit(-1);
        }
    }

    public static void exitOnGLError(String errorMessage) {
        int errorValue = GL11.glGetError();
        if (errorValue != GL11.GL_NO_ERROR) {
            String errorString = GLU.gluErrorString(errorValue);
            logger.error("ERROR - " + errorMessage + ": " + errorString);
            if (Display.isCreated()) Display.destroy(); {
                System.exit(-1);
            }
        }
    }
}
