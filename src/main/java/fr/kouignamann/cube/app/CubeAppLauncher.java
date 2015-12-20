package fr.kouignamann.cube.app;

import fr.kouignamann.cube.core.*;
import fr.kouignamann.cube.core.listener.*;
import fr.kouignamann.cube.core.utils.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.slf4j.*;

public class CubeAppLauncher {

    private static Logger logger = LoggerFactory.getLogger(CubeAppLauncher.class);

    public CubeAppLauncher() throws LWJGLException {

        GlUtils.initLwjglLibs();
        CubeAppGraphics.setup();
        CubeAppLogics.initLogics();

        CubeAppListeners.startListeners();

        // Main loop
        while (!Display.isCloseRequested()) {
            CubeAppLogics.compute();
            CubeAppGraphics.draw();
            Display.update();
        }

        CubeAppListeners.stopListeners();
        CubeAppLogics.destroyLogics();
        CubeAppGraphics.destroy();
    }

    public static void main( String[] args ) throws Exception {
        logger.info(Constant.WINDOW_NAME);
        new CubeAppLauncher();
    }
}
