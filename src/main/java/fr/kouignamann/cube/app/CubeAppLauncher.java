package fr.kouignamann.cube.app;

import fr.kouignamann.cube.core.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.slf4j.*;

import java.lang.reflect.*;

public class CubeAppLauncher {

    private static Logger logger = LoggerFactory.getLogger(CubeAppLauncher.class);

    static {
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
