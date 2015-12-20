package fr.kouignamann.cube.core;

import fr.kouignamann.cube.core.model.drawable.*;
import fr.kouignamann.cube.core.model.drawable.shader.*;
import fr.kouignamann.cube.core.model.gl.*;
import fr.kouignamann.cube.core.utils.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.slf4j.*;

import java.util.*;

import static fr.kouignamann.cube.core.Constant.*;

public class CubeAppGraphics {

    private static CubeAppGraphics graphics;

    private static Logger logger = LoggerFactory.getLogger(CubeAppGraphics.class);

    private ShaderObject cubaAppShader;

    private Camera cubeAppCamera;

    private int[] textureIds;

    private int textureIdsArrayIndex;

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
        graphics.cubaAppShader = new ShaderObject();
        graphics.cubeAppCamera = new Camera();
        graphics.textureIds = new int[] {
                TextureUtils.loadTexture("/textures/glass.png"),
                TextureUtils.loadTexture("/textures/mask.png"),
                TextureUtils.loadTexture("/textures/lol.png"),
                TextureUtils.loadTexture("/textures/notSureIf.png")
        };
        graphics.textureIdsArrayIndex = 0;
    }

    public static void destroy() {
        logger.info("Destroying Graphics");
        checkCtx();
        graphics.cubaAppShader.destroy();
        TextureUtils.destroyTextures(graphics.textureIds);
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

            Display.setDisplayMode(new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT));
            Display.setTitle(WINDOW_NAME);
            Display.create(pixelFormat, contextAtrributes);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        GL11.glClearColor(BLUE[0], BLUE[1], BLUE[2], BLUE[3]);
        GL11.glClearStencil(0);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
        GL11.glViewport(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public static void draw() {
        checkCtx();
        graphics.cubeAppCamera.compute();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL20.glUseProgram(graphics.cubaAppShader.getShaderProgramId());
        graphics.cubaAppShader.pushUniforms(graphics.cubeAppCamera);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, graphics.textureIds[graphics.textureIdsArrayIndex]);

        List<DrawableObject> drawables = CubeAppLogics.getDrawables();
        for (int i=0; i < drawables.size(); i++) {
            GL11.glStencilFunc(GL11.GL_ALWAYS, i+1, -1);
            DrawableObject drawableObject = drawables.get(i);
            GL30.glBindVertexArray(drawableObject.getVaoId());
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
            GL20.glEnableVertexAttribArray(2);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, drawableObject.getVboiId());
            GL11.glDrawElements(GL11.GL_TRIANGLES, drawableObject.getNbIndices(), GL11.GL_UNSIGNED_INT, 0);
        }
//        for (DrawableObject drawable : drawables) {
//            GL30.glBindVertexArray(drawable.getVaoId());
//            GL20.glEnableVertexAttribArray(0);
//            GL20.glEnableVertexAttribArray(1);
//            GL20.glEnableVertexAttribArray(2);
//            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, drawable.getVboiId());
//            GL11.glDrawElements(GL11.GL_TRIANGLES, drawable.getNbIndices(), GL11.GL_UNSIGNED_INT, 0);
//        }

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
        GL20.glUseProgram(0);
    }

    public static void previousTexture() {
        checkCtx();
        graphics.textureIdsArrayIndex = (graphics.textureIdsArrayIndex - 1 + graphics.textureIds.length) % graphics.textureIds.length;
    }

    public static void nextTexture() {
        checkCtx();
        graphics.textureIdsArrayIndex = (graphics.textureIdsArrayIndex +1) % graphics.textureIds.length;
    }

    public static void addCameraMovement(float movement) {
        checkCtx();
        graphics.cubeAppCamera.addMovement(movement);
    }

    public static void addCameraRotation(float deltaX, float deltaY) {
        checkCtx();
        graphics.cubeAppCamera.addRotation(deltaX, deltaY);
    }

    public static Camera getCamera() {
        checkCtx();
        return graphics.cubeAppCamera;
    }

    private static void checkCtx() {
        if (graphics == null) {
            throw new IllegalStateException("CubeAppGraphics is null");
        }
    }
}
