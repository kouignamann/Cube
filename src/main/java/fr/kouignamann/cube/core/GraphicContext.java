package fr.kouignamann.cube.core;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static fr.kouignamann.cube.core.Constant.*;

public class GraphicContext {

    private static GraphicContext ctx;

    private static Logger logger = LoggerFactory.getLogger(GraphicContext.class);

    private GraphicContext() {
        super();
    }

    public static void setup() {
        logger.info("Setting up Graphics");
        GraphicContext.checkCtx();
        GraphicContext.setupOpenGL();
    }

    public static void destroy() {
        logger.info("Destroying Graphics");
        GraphicContext.checkCtx();
        GraphicContext.exitOnGLError("destroyOpenGL");
        Display.destroy();
    }

    private static void checkCtx() {
        if (ctx == null) {
            ctx = new GraphicContext();
        }
    }

    private static void setupOpenGL() {
        try {
            PixelFormat pixelFormat = new PixelFormat();
            ContextAttribs contextAtrributes = new ContextAttribs(3, 2)
                    .withForwardCompatible(true)
                    .withProfileCore(true);

            Display.setDisplayMode(new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGTH));
            Display.setTitle(WINDOW_NAME);
            Display.create(pixelFormat, contextAtrributes);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        GL11.glClearColor(0f, 0f, 0f, 0f);
        GL11.glViewport(0, 0, SCREEN_WIDTH, SCREEN_HEIGTH);
    }

    private static void exitOnGLError(String errorMessage) {
        int errorValue = GL11.glGetError();
        if (errorValue != GL11.GL_NO_ERROR) {
            String errorString = GLU.gluErrorString(errorValue);
            System.err.println("ERROR - " + errorMessage + ": " + errorString);
            if (Display.isCreated()) Display.destroy(); {
                System.exit(-1);
            }
        }
    }
}
