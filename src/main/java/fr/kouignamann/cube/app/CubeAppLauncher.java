package fr.kouignamann.cube.app;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.kouignamann.cube.core.Constant;
import fr.kouignamann.cube.core.CubeAppGraphics;
import fr.kouignamann.cube.core.CubeAppLogics;
import fr.kouignamann.cube.core.CubeAppTextures;
import fr.kouignamann.cube.core.listener.CubeAppListeners;
import fr.kouignamann.cube.core.utils.GlUtils;

public class CubeAppLauncher {

    private static Logger logger = LoggerFactory.getLogger(CubeAppLauncher.class);

    public CubeAppLauncher() throws LWJGLException {

        GlUtils.initLwjglLibs();
        CubeAppGraphics.setup();
        CubeAppTextures.setup();
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
        CubeAppTextures.destroy();
        CubeAppGraphics.destroy();
    }

    public static void main( String[] args ) throws Exception {
        logger.info(Constant.WINDOW_NAME);
        new CubeAppLauncher();
    }
}
