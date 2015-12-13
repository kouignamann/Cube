package fr.kouignamann.cube.app;

import fr.kouignamann.cube.core.Constant;
import fr.kouignamann.cube.core.CubeAppGraphics;
import fr.kouignamann.cube.core.CubeAppLogics;
import fr.kouignamann.cube.core.utils.GlUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CubeAppLauncher {

    private static Logger logger = LoggerFactory.getLogger(CubeAppLauncher.class);

    public CubeAppLauncher() throws LWJGLException {

        GlUtils.initLwjglLibs();
        CubeAppGraphics.setup();
        CubeAppLogics.initLogics();

        // Main loop
        while (!Display.isCloseRequested()) {
            CubeAppGraphics.draw();
            Display.update();
        }

        CubeAppLogics.destroyLogics();
        CubeAppGraphics.destroy();
    }

    public static void main( String[] args ) throws Exception {
        logger.info(Constant.WINDOW_NAME);
        new CubeAppLauncher();
    }
}
