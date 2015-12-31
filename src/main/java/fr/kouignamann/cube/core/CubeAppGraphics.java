package fr.kouignamann.cube.core;

import static fr.kouignamann.cube.core.Constant.*;

import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.PixelFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.kouignamann.cube.core.model.drawable.DrawableObject;
import fr.kouignamann.cube.core.model.drawable.DrawableObjectPart;
import fr.kouignamann.cube.core.model.drawable.shader.ShaderObject;
import fr.kouignamann.cube.core.model.drawable.shader.SimpleCubeShader;
import fr.kouignamann.cube.core.model.gl.Camera;
import fr.kouignamann.cube.core.model.gl.Vertex;
import fr.kouignamann.cube.core.utils.GlUtils;

public class CubeAppGraphics {

    private static CubeAppGraphics graphics;

    private static Logger logger = LoggerFactory.getLogger(CubeAppGraphics.class);

    private ShaderObject cubaAppShader;

    private Camera cubeAppCamera;

    private CubeAppGraphics() {
        super();
    }

    public static void setup() {
        logger.info("Setting up Graphics");
        if (graphics != null) {
            throw new IllegalStateException("CubeAppGraphics already initialized");
        }
        graphics = new CubeAppGraphics();
        CubeAppGraphics.setupOpenGL();
        graphics.cubaAppShader = new SimpleCubeShader();
        graphics.cubeAppCamera = new Camera();
    }

    public static void destroy() {
        logger.info("Destroying Graphics");
        checkCtx();
        graphics.cubaAppShader.destroy();
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

        GL11.glClearColor(BLACK[0], BLACK[1], BLACK[2], BLACK[3]);
        GL11.glClearStencil(0);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glViewport(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public static void draw() {
        checkCtx();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL20.glUseProgram(graphics.cubaAppShader.getShaderProgramId());
        graphics.cubaAppShader.pushUniforms(graphics.cubeAppCamera.compute());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);

        List<DrawableObject> drawables = CubeAppLogics.getDrawables();
        for (int i=0; i < drawables.size(); i++) {
            DrawableObject drawableObject = drawables.get(i);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, drawableObject.getTextureId());
            GL30.glBindVertexArray(drawableObject.getVaoId());
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
            GL20.glEnableVertexAttribArray(2);
            GL20.glEnableVertexAttribArray(3);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, drawableObject.getVboiId());

            for (DrawableObjectPart drawableObjectPart : drawableObject.getParts()) {
                GL11.glDrawElements(
                        GL11.GL_TRIANGLES,
                        drawableObjectPart.getLength(),
                        GL11.GL_UNSIGNED_INT,
                        drawableObjectPart.getStartIndex()*Vertex.ELEMENT_BYTES);
            }
        }

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
        GL20.glUseProgram(0);
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
