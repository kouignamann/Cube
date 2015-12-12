package fr.kouignamann.cube.app;

import fr.kouignamann.cube.core.Constant;
import fr.kouignamann.cube.core.GraphicContext;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

public class CubeAppLauncher {

    private static Logger logger = LoggerFactory.getLogger(CubeAppLauncher.class);

    static {
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

    public CubeAppLauncher() throws LWJGLException {

        GraphicContext.setup();

        // Main loop
        while (!Display.isCloseRequested()) {
            Display.update();
        }

        GraphicContext.destroy();
    }

    public static void main( String[] args ) throws Exception {
        logger.info(Constant.WINDOW_NAME);
        new CubeAppLauncher();
    }
}
