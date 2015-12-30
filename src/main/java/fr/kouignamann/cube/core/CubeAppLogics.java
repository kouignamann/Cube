package fr.kouignamann.cube.core;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.kouignamann.cube.core.builder.CubeBuilder;
import fr.kouignamann.cube.core.model.drawable.DrawableObject;
import fr.kouignamann.cube.core.model.drawable.DrawableObjectPart;
import fr.kouignamann.cube.core.model.drawable.shader.SelectionCubeShader;
import fr.kouignamann.cube.core.model.drawable.shader.ShaderObject;
import fr.kouignamann.cube.core.model.gl.SelectionColor;
import fr.kouignamann.cube.core.model.gl.Vertex;

public class CubeAppLogics {

    private static Logger logger = LoggerFactory.getLogger(CubeAppLogics.class);

    private static CubeAppLogics logics;

    private List<DrawableObject> dObjects;

    private ShaderObject selectionShader;

    private int xClicked = -1;
    private int yClicked = -1;
    private long lastClick = 0;

    private float currentCubeScale = 100.0f;
    private Vector3f currentCubeRotation = new Vector3f(0.0f, 0.0f, 0.0f);
    private boolean cubeGeometryHasChanged = false;

    private CubeAppLogics() {
        dObjects = new ArrayList<>();
    }

    public static void initLogics() {
        if (logics != null) {
            logics.dObjects.stream().forEach(DrawableObject::destroy);
        }
        logics = new CubeAppLogics();

        logics.dObjects.add(CubeBuilder.build3x3x3Cubes());
        logics.selectionShader = new SelectionCubeShader();
    }

    private static void checkCtx() {
        if (logics == null) {
            throw new IllegalStateException("CubeAppLogics is null");
        }
    }

    public static void destroyLogics() {
        checkCtx();
        logics.dObjects.stream().forEach(DrawableObject::destroy);
        logics.selectionShader.destroy();
        logics = null;
    }

    synchronized public static void compute() {
        checkCtx();
        if (logics.xClicked>=0) {
            printClickCoord();
        }
        if (logics.cubeGeometryHasChanged) {
            for (DrawableObject drawableObject : logics.dObjects) {
                CubeBuilder.changeDrawableGeometry(drawableObject, logics.currentCubeScale, logics.currentCubeRotation);
            }
            logics.cubeGeometryHasChanged = false;
        }
    }

    private static void printClickCoord() {
        checkCtx();
        if (Constant.CLICK_MS_COOLDOWN + logics.lastClick < System.currentTimeMillis()) {
            logics.lastClick = System.currentTimeMillis();

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL20.glUseProgram(logics.selectionShader.getShaderProgramId());
            logics.selectionShader.pushUniforms(CubeAppGraphics.getCamera().compute());
            for (int i=0; i < logics.dObjects.size(); i++) {
                DrawableObject drawableObject = logics.dObjects.get(i);
                GL30.glBindVertexArray(drawableObject.getVaoId());
                GL20.glEnableVertexAttribArray(0);
                GL20.glEnableVertexAttribArray(2);
                GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, drawableObject.getVboiId());
                for (DrawableObjectPart drawableObjectPart : drawableObject.getParts()) {
                    GL11.glDrawElements(
                            GL11.GL_TRIANGLES,
                            drawableObjectPart.getLength(),
                            GL11.GL_UNSIGNED_INT,
                            drawableObjectPart.getStartIndex()* Vertex.ELEMENT_BYTES);
                }
            }
            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(2);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
            GL30.glBindVertexArray(0);
            GL20.glUseProgram(0);

            FloatBuffer pixelBuffer = BufferUtils.createFloatBuffer(4);
            IntBuffer viewBuffer = BufferUtils.createIntBuffer(16);
            GL11.glGetInteger(GL11.GL_VIEWPORT, viewBuffer);
            GL11.glReadPixels(logics.xClicked, logics.yClicked, 1, 1, GL11.GL_RGBA, GL11.GL_FLOAT, pixelBuffer);

            SelectionColor selectedColor = SelectionColor.fromFloatBuffer(pixelBuffer);
            DrawableObjectPart selectedPart = null;
            drawableLoop : for (DrawableObject drawableObject : logics.dObjects) {
                drawablePartLoop : for (DrawableObjectPart drawableObjectPart : drawableObject.getParts()) {
                    if (drawableObjectPart.getSelectionColor().rgbEquals(selectedColor)) {
                        selectedPart = drawableObjectPart;
                        break drawableLoop;
                    }
                }
            }
            logger.info(String.format("Selected part vertice : startIndex = %d, length = %d", selectedPart.getStartVertexIndex(), selectedPart.getNbVertex()));
        }
        logics.xClicked = -1;
        logics.yClicked = -1;
    }

    public static List<DrawableObject> getDrawables() {
        checkCtx();
        return logics.dObjects;
    }

    synchronized public static void registerScreenClick(int x, int y) {
        checkCtx();
        logics.xClicked = x;
        logics.yClicked = y;
    }

    synchronized public static void registerCubeScale(boolean increaseScale) {
        checkCtx();
        float newScale = logics.currentCubeScale + (increaseScale ? 1f : -1f) * Constant.CUBE_SCALE_SPEED;
        if (newScale >= 1 && newScale <= 100) {
            logics.currentCubeScale = newScale;
            logics.cubeGeometryHasChanged = true;
        }
    }

    synchronized public static void registerXCubeRotation(boolean directRotation) {
        checkCtx();
        float xRotation = logics.currentCubeRotation.getX();
        logics.currentCubeRotation.setX(xRotation + (directRotation ? 1f : -1f) * Constant.CUBE_ROTATION_SPEED);
        logics.cubeGeometryHasChanged = true;
    }

    synchronized public static void registerYCubeRotation(boolean directRotation) {
        checkCtx();
        float yRotation = logics.currentCubeRotation.getY();
        logics.currentCubeRotation.setY(yRotation + (directRotation ? 1f : -1f) * Constant.CUBE_ROTATION_SPEED);
        logics.cubeGeometryHasChanged = true;
    }

    synchronized public static void registerZCubeRotation(boolean directRotation) {
        checkCtx();
        float zRotation = logics.currentCubeRotation.getZ();
        logics.currentCubeRotation.setZ(zRotation + (directRotation ? 1f : -1f) * Constant.CUBE_ROTATION_SPEED);
        logics.cubeGeometryHasChanged = true;
    }
}
