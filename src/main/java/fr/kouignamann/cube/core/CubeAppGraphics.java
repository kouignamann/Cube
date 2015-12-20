package fr.kouignamann.cube.core;

import fr.kouignamann.cube.core.model.drawable.*;
import fr.kouignamann.cube.core.model.drawable.shader.*;
import fr.kouignamann.cube.core.utils.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.slf4j.*;

import java.nio.*;
import java.util.*;

import static fr.kouignamann.cube.core.Constant.*;

public class CubeAppGraphics {

    private static CubeAppGraphics graphics;

    private static Logger logger = LoggerFactory.getLogger(CubeAppGraphics.class);

    private ShaderObject cubeAppShader;
    private ShaderObject finalRenderShader;

    private int frameBufferId;
    private int colorTextureId;
//    private int depthRenderBufferId;

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
        checkCtx();
        setupOpenGL();
        setupFrameBufferObject();
        graphics.cubeAppShader = new SimpleShader();
        graphics.finalRenderShader = new FinalShader();
        graphics.textureIds = new int[] {
                TextureUtils.loadTexture("/textures/square-metal-plate.jpg"),
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
        ShaderUtils.destroyCubeAppShader(graphics.cubeAppShader);
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

            Display.setDisplayMode(new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGTH));
            Display.setTitle(WINDOW_NAME);
            Display.create(pixelFormat, contextAtrributes);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        GL11.glClearColor(BLACK[0], BLACK[1], BLACK[2], BLACK[3]);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glViewport(0, 0, SCREEN_WIDTH, SCREEN_HEIGTH);
    }

    public static void draw() {
        checkCtx();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL20.glUseProgram(graphics.cubeAppShader.getShaderProgramId());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, graphics.textureIds[graphics.textureIdsArrayIndex]);

        List<DrawableObject> drawables = CubeAppLogics.getDrawables();
        for (DrawableObject drawable : drawables) {
            GL30.glBindVertexArray(drawable.getVaoId());
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
            GL20.glEnableVertexAttribArray(2);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, drawable.getVboiId());
            GL11.glDrawElements(GL11.GL_TRIANGLES, drawable.getNbIndices(), GL11.GL_UNSIGNED_INT, 0);
        }

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
        GL20.glUseProgram(0);
    }

    public static void drawFBO() {
        checkCtx();

        //http://stackoverflow.com/questions/7357626/framebuffer-and-using-shaders-in-opengl
        //http://wiki.lwjgl.org/wiki/Render_to_Texture_with_Frame_Buffer_Objects_(FBO)
        //http://http.developer.nvidia.com/GPUGems/gpugems_ch21.html
        //http://www.learnopengl.com/#!Advanced-Lighting/Bloom

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, graphics.frameBufferId);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL20.glUseProgram(graphics.cubeAppShader.getShaderProgramId());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, graphics.textureIds[graphics.textureIdsArrayIndex]);

        List<DrawableObject> drawables = CubeAppLogics.getDrawables();
        for (DrawableObject drawable : drawables) {
            GL30.glBindVertexArray(drawable.getVaoId());
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
            GL20.glEnableVertexAttribArray(2);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, drawable.getVboiId());
            GL11.glDrawElements(GL11.GL_TRIANGLES, drawable.getNbIndices(), GL11.GL_UNSIGNED_INT, 0);
        }

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);


        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL20.glUseProgram(graphics.finalRenderShader.getShaderProgramId());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, graphics.colorTextureId);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, graphics.colorTextureId);

        renderFinalScene();
        GL20.glUseProgram(0);
    }

    private int quadVAO = 0;
    private int quadVBO;

    private static void renderFinalScene() {
        // RenderQuad() Renders a 1x1 quad in NDC, best used for framebuffer color targets
        // and post-processing effects.
        checkCtx();
        if (graphics.quadVAO == 0) {
            float[] quadVertices = new float[]{
                    // Positions            // Texture Coords
                    -1.0f, 1.0f, 0.0f,      0.0f, 1.0f,
                    -1.0f, -1.0f, 0.0f,     0.0f, 0.0f,
                    1.0f, 1.0f, 0.0f,       1.0f, 1.0f,
                    1.0f, -1.0f, 0.0f,      1.0f, 0.0f
            };
            FloatBuffer quadBuffer = BufferUtils.createFloatBuffer(quadVertices.length);
            quadBuffer.put(quadVertices);
            quadBuffer.flip();

            graphics.quadVAO = GL30.glGenVertexArrays();
            graphics.quadVBO = GL15.glGenBuffers();
            GL30.glBindVertexArray(graphics.quadVAO);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, graphics.quadVBO);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, quadBuffer, GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 20, 0);
            GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 20, 12);
        }
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL30.glBindVertexArray(graphics.quadVAO);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
        GL30.glBindVertexArray(0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
    }

    private static void setupFrameBufferObject() {
        checkCtx();
        graphics.frameBufferId = GL30.glGenFramebuffers();
        graphics.colorTextureId = GL11.glGenTextures();

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, graphics.frameBufferId);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, graphics.colorTextureId);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, SCREEN_WIDTH, SCREEN_HEIGTH, 0, GL11.GL_RGBA, GL11.GL_INT, (ByteBuffer) null);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, graphics.colorTextureId, 0);

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

    }

    public static void previousTexture() {
        checkCtx();
        graphics.textureIdsArrayIndex = (graphics.textureIdsArrayIndex - 1 + graphics.textureIds.length) % graphics.textureIds.length;
    }

    public static void nextTexture() {
        checkCtx();
        graphics.textureIdsArrayIndex = (graphics.textureIdsArrayIndex +1) % graphics.textureIds.length;
    }

    private static void checkCtx() {
        if (graphics == null) {
            throw new IllegalStateException("CubeAppGraphics is null");
        }
    }
}
