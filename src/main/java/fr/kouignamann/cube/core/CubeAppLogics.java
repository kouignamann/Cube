package fr.kouignamann.cube.core;

import fr.kouignamann.cube.core.builder.*;
import fr.kouignamann.cube.core.model.drawable.*;
import fr.kouignamann.cube.core.model.drawable.shader.*;
import fr.kouignamann.cube.core.model.gl.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.slf4j.*;

import java.nio.*;
import java.util.*;

public class CubeAppLogics {

    private static Logger logger = LoggerFactory.getLogger(CubeAppLogics.class);

    private static CubeAppLogics logics;

    private List<DrawableObject> dObjects;

    private DrawableObjectPart selectedDrawableObjectPart;

    private ShaderObject selectionShader;

    private int xSelectionClick = -1;
    private int ySelectionClick = -1;

    private CubeAppLogics() {
        dObjects = new ArrayList<>();
    }

    public static void initLogics() {
        if (logics != null) {
            logics.dObjects.stream().forEach(DrawableObject::destroy);
        }
        logics = new CubeAppLogics();

        logics.dObjects.add(CubeBuilder.build3x3x3Cubes());
        logics.dObjects.add(QuadBuilder.buildGround());
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
        if (logics.xSelectionClick >=0) {
            findClickedDrawanbleObjectPart();
        }
        logics.dObjects.stream().forEach(DrawableObject::computeGeometry);
    }

    private static void findClickedDrawanbleObjectPart() {
        checkCtx();

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
        GL11.glReadPixels(logics.xSelectionClick, logics.ySelectionClick, 1, 1, GL11.GL_RGBA, GL11.GL_FLOAT, pixelBuffer);

        SelectionColor selectedColor = SelectionColor.fromFloatBuffer(pixelBuffer);
        if (!selectedColor.nothingSelected()) {
            DrawableObjectPart selectedPart = null;
            mainLoop:
            for (DrawableObject drawableObject : logics.dObjects) {
                for (DrawableObjectPart drawableObjectPart : drawableObject.getParts()) {
                    if (drawableObjectPart.getSelectionColor().equalsRGB(selectedColor)) {
                        selectedPart = drawableObjectPart;
                        break mainLoop;
                    }
                }
            }
            if (selectedPart != null) {
                if (logics.selectedDrawableObjectPart != null) {
                    List<Vertex> selectedVertice = logics.selectedDrawableObjectPart.readVertice();
                    selectedVertice.forEach(v -> v.setColor(Constant.RED));
                    logics.selectedDrawableObjectPart.pushVertice(selectedVertice);
                }
                logics.selectedDrawableObjectPart = selectedPart;
                List<Vertex> selectedVertice = logics.selectedDrawableObjectPart.readVertice();
                selectedVertice.forEach(v -> v.setColor(Constant.GREEN));
                logics.selectedDrawableObjectPart.pushVertice(selectedVertice);
            }
        }
        else if (logics.selectedDrawableObjectPart != null) {
            List<Vertex> selectedVertice = logics.selectedDrawableObjectPart.readVertice();
            selectedVertice.forEach(v -> v.setColor(Constant.RED));
            logics.selectedDrawableObjectPart.pushVertice(selectedVertice);
            logics.selectedDrawableObjectPart = null;
        }
        logics.xSelectionClick = -1;
        logics.ySelectionClick = -1;
    }

    public static List<DrawableObject> getDrawables() {
        checkCtx();
        return logics.dObjects;
    }

    synchronized public static void registerScreenClick(int x, int y) {
        checkCtx();
        logics.xSelectionClick = x;
        logics.ySelectionClick = y;
    }

    synchronized public static void registerCubeScale(boolean increaseScale) {
        checkCtx();
        if (logics.selectedDrawableObjectPart != null) {
            float newScale = logics.selectedDrawableObjectPart.getScale() + (increaseScale ? 1f : -1f) * Constant.CUBE_SCALE_SPEED;
            if (newScale >= 1 && newScale <= 100) {
                logics.selectedDrawableObjectPart.setScale(newScale);
            }
        } else {
            for (DrawableObject object : logics.dObjects) {
                for (DrawableObjectPart part : object.getParts()) {
                    float newScale = part.getScale() + (increaseScale ? 1f : -1f) * Constant.CUBE_SCALE_SPEED;
                    if (newScale >= 1 && newScale <= 100) {
                        part.setScale(newScale);
                    }
                }
            }
        }
    }

    synchronized public static void registerXCubeRotation(boolean directRotation) {
        checkCtx();
        if (logics.selectedDrawableObjectPart != null) {
            logics.selectedDrawableObjectPart.getRotation().setX(
                    logics.selectedDrawableObjectPart.getRotation().getX() +
                    (directRotation ? 1f : -1f) * Constant.CUBE_ROTATION_SPEED);
        } else {
            for (DrawableObject object : logics.dObjects) {
                for (DrawableObjectPart part : object.getParts()) {
                    part.getRotation().setX(part.getRotation().getX() + (directRotation ? 1f : -1f) * Constant.CUBE_ROTATION_SPEED);
                }
            }
        }
    }

    synchronized public static void registerYCubeRotation(boolean directRotation) {
        checkCtx();
        if (logics.selectedDrawableObjectPart != null) {
            logics.selectedDrawableObjectPart.getRotation().setY(
                    logics.selectedDrawableObjectPart.getRotation().getY() +
                    (directRotation ? 1f : -1f) * Constant.CUBE_ROTATION_SPEED);
        } else {
            for (DrawableObject object : logics.dObjects) {
                for (DrawableObjectPart part : object.getParts()) {
                    part.getRotation().setY(part.getRotation().getY() + (directRotation ? 1f : -1f) * Constant.CUBE_ROTATION_SPEED);
                }
            }
        }
    }

    synchronized public static void registerZCubeRotation(boolean directRotation) {
        checkCtx();
        if (logics.selectedDrawableObjectPart != null) {
            logics.selectedDrawableObjectPart.getRotation().setZ(
                    logics.selectedDrawableObjectPart.getRotation().getZ() +
                            (directRotation ? 1f : -1f) * Constant.CUBE_ROTATION_SPEED);
        } else {
            for (DrawableObject object : logics.dObjects) {
                for (DrawableObjectPart part : object.getParts()) {
                    part.getRotation().setZ(part.getRotation().getZ() + (directRotation ? 1f : -1f) * Constant.CUBE_ROTATION_SPEED);
                }
            }
        }
    }
}
