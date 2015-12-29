package fr.kouignamann.cube.core.utils;

import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.*;
import org.slf4j.*;

import java.lang.reflect.*;

public class GlUtils {

    private static Logger logger = LoggerFactory.getLogger(GlUtils.class);

    public static void initLwjglLibs() {
        try {
            logger.info("Loading natives");
            java.awt.Toolkit.getDefaultToolkit();
            String javaLibPath = System.getProperty("java.library.path");
            String pathSeparator = System.getProperty("path.separator");
            System.setProperty(
                    "java.library.path",
                    javaLibPath + pathSeparator + "target/executable/natives/" + pathSeparator + "natives/");
            Field sysPath = ClassLoader.class.getDeclaredField("sys_paths");
            sysPath.setAccessible(true);
            sysPath.set(null, null);
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
