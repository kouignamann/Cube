package fr.kouignamann.cube.core;

import fr.kouignamann.cube.core.model.drawable.DrawableObject;
import fr.kouignamann.cube.core.utils.GlUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static fr.kouignamann.cube.core.Constant.*;

public class CubeAppGraphics {

    private static CubeAppGraphics graphics;

    private static Logger logger = LoggerFactory.getLogger(CubeAppGraphics.class);

    private CubeAppGraphics() {
        super();
    }

    public static void setup() {
        logger.info("Setting up Graphics");
        if (graphics != null) {
            throw new IllegalStateException("CubeAppGraphics already initialized");
        }
        graphics = new CubeAppGraphics();
        CubeAppGraphics.checkCtx();
        CubeAppGraphics.setupOpenGL();
    }

    public static void destroy() {
        logger.info("Destroying Graphics");
        checkCtx();
        GlUtils.exitOnGLError("CubeAppGraphics destruction failure");
        Display.destroy();
    }

    private static void setupOpenGL() {
        checkCtx();
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

        GL11.glClearColor(BLUE[0], BLUE[1], BLUE[2], BLUE[3]);
        GL11.glViewport(0, 0, SCREEN_WIDTH, SCREEN_HEIGTH);
    }

    public static void draw() {
        checkCtx();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        List<DrawableObject> drawables = CubeAppLogics.getDrawables();
        for (DrawableObject drawable : drawables) {
            GL30.glBindVertexArray(drawable.getVaoId());
            GL20.glEnableVertexAttribArray(0);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, drawable.getVboiId());
            GL11.glDrawElements(GL11.GL_TRIANGLES, drawable.getNbIndices(), GL11.GL_UNSIGNED_INT, 0);
        }

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    private static void checkCtx() {
        if (graphics == null) {
            throw new IllegalStateException("CubeAppGraphics is null");
        }
    }
}
