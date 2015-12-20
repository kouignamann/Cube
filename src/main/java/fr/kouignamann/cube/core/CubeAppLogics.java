package fr.kouignamann.cube.core;

import fr.kouignamann.cube.core.builder.*;
import fr.kouignamann.cube.core.model.drawable.*;
import fr.kouignamann.cube.core.model.gl.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.*;
import org.slf4j.*;

import java.nio.*;
import java.util.*;

public class CubeAppLogics {

    private static Logger logger = LoggerFactory.getLogger(CubeAppLogics.class);

    private static CubeAppLogics logics;

    private List<DrawableObject> dObjects;

    private int xClicked = -1;
    private int yClicked = -1;
    private long lastClick = 0;

    private CubeAppLogics() {
        dObjects = new ArrayList<>();
    }

    public static void initLogics() {
        if (logics != null) {
            logics.dObjects.stream().forEach(DrawableObject::destroy);
        }
        logics = new CubeAppLogics();

        logics.dObjects.add(CubeBuilder.build3x3x3Cubes());
    }

    public static void destroyLogics() {
        if (logics == null) {
            throw new IllegalStateException("CubeAppLogics is null");
        }
        logics.dObjects.stream().forEach(DrawableObject::destroy);
        logics = null;
    }

    synchronized public static void compute() {
        if (logics.xClicked>=0) {
            printClickCoord();
        }
    }

    private static void printClickCoord() {
        if (Constant.CLICK_MS_COOLDOWN + logics.lastClick < System.currentTimeMillis()) {
            logics.lastClick = System.currentTimeMillis();
            Camera camera = CubeAppGraphics.getCamera();
            camera.compute();
            logger.info(String.format("Clicked at (%d | %d) - mouse coord", logics.xClicked, logics.yClicked));

            FloatBuffer modelBuffer = BufferUtils.createFloatBuffer(16);
            FloatBuffer projectionBuffer = BufferUtils.createFloatBuffer(16);
            IntBuffer viewBuffer = BufferUtils.createIntBuffer(16);
            camera.getModelMatrix().store(modelBuffer);
            camera.getProjectionMatrix().store(projectionBuffer);
            modelBuffer.rewind();
            projectionBuffer.rewind();

            GL11.glGetInteger(GL11.GL_VIEWPORT, viewBuffer);
            viewBuffer.rewind();
            FloatBuffer pos = BufferUtils.createFloatBuffer(16);
            FloatBuffer z = BufferUtils.createFloatBuffer(1);
            GL11.glReadPixels(logics.xClicked, logics.yClicked, 1, 1, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, z);

            logger.info("z from depth buffer : " + z.get(0));

            GLU.gluUnProject(logics.xClicked, logics.yClicked, z.get(0), modelBuffer, projectionBuffer, viewBuffer, pos);
            logger.info(String.format("Clicked at (%f | %f | %f | %f) - position vect", pos.get(0), pos.get(1), pos.get(2), pos.get(3)));
        }
        logics.xClicked = -1;
        logics.yClicked = -1;
    }

    public static List<DrawableObject> getDrawables() {
        if (logics==null) {
            throw new IllegalStateException("CubeAppLogics wasn t initialized properly (use 'initLogics')");
        }
        return logics.dObjects;
    }

    synchronized public static void registerScreenClick(int x, int y) {
        logics.xClicked = x;
        logics.yClicked = y;
    }
}
