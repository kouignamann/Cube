package fr.kouignamann.cube.core.builder;

import fr.kouignamann.cube.core.model.drawable.*;
import fr.kouignamann.cube.core.model.gl.*;

import java.nio.*;
import java.util.*;

import static fr.kouignamann.cube.core.Constant.*;

public class CubeBuilder extends DrawableObjectBuilder {

    private final static float FACE_WIDTH = 100f;
    private final static float FACE_MARGIN = 1f;
    private final static float FACE_REAL_WIDTH = FACE_WIDTH - FACE_MARGIN;
    private final static float CUBE_UNIT = FACE_REAL_WIDTH / 2.0f;

    private final static int[] FACE_INDICES = {
            0,	1,	2,
            2,	3,	0
    };

    private static List<Vertex> getCubeVectors(int abs, int ord, int z) {
        return Arrays.asList(
                new Vertex(-CUBE_UNIT,  -CUBE_UNIT, CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 0f),    // FRONT FACE
                new Vertex(CUBE_UNIT,   -CUBE_UNIT, CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 1f),    // FRONT FACE
                new Vertex(CUBE_UNIT,   CUBE_UNIT,  CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 1f),    // FRONT FACE
                new Vertex(-CUBE_UNIT,  CUBE_UNIT,  CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 0f),    // FRONT FACE

                new Vertex(CUBE_UNIT,   -CUBE_UNIT, CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 0f),    // RIGHT FACE
                new Vertex(CUBE_UNIT,   -CUBE_UNIT, -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 1f),   // RIGHT FACE
                new Vertex(CUBE_UNIT,   CUBE_UNIT,  -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 1f),   // RIGHT FACE
                new Vertex(CUBE_UNIT,   CUBE_UNIT,  CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 0f),    // RIGHT FACE

                new Vertex(CUBE_UNIT,   -CUBE_UNIT, -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 0f),   // BACK FACE
                new Vertex(-CUBE_UNIT,  -CUBE_UNIT, -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 1f),   // BACK FACE
                new Vertex(-CUBE_UNIT,  CUBE_UNIT,  -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 1f),   // BACK FACE
                new Vertex(CUBE_UNIT,   CUBE_UNIT,  -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 0f),   // BACK FACE

                new Vertex(-CUBE_UNIT,  -CUBE_UNIT, -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 0f),   // LEFT FACE
                new Vertex(-CUBE_UNIT,  -CUBE_UNIT, CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 1f),    // LEFT FACE
                new Vertex(-CUBE_UNIT,  CUBE_UNIT,  CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 1f),    // LEFT FACE
                new Vertex(-CUBE_UNIT,  CUBE_UNIT,  -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 0f),   // LEFT FACE

                new Vertex(-CUBE_UNIT,  CUBE_UNIT,  CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 0f),    // TOP FACE
                new Vertex(CUBE_UNIT,   CUBE_UNIT,  CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 1f),    // TOP FACE
                new Vertex(CUBE_UNIT,   CUBE_UNIT,  -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 1f),   // TOP FACE
                new Vertex(-CUBE_UNIT,  CUBE_UNIT,  -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 0f),   // TOP FACE

                new Vertex(CUBE_UNIT,   -CUBE_UNIT, -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 0f),   // BOTTOM FACE
                new Vertex(-CUBE_UNIT,  -CUBE_UNIT, -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 1f),   // BOTTOM FACE
                new Vertex(-CUBE_UNIT,  -CUBE_UNIT, CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 1f),    // BOTTOM FACE
                new Vertex(CUBE_UNIT,   -CUBE_UNIT, CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 0f)     // BOTTOM FACE
        );
    }

    public static DrawableObject buildCube() {
        List<Vertex> faceVertices = getCubeVectors(0, 0, 0);
        faceVertices.stream().forEach(v -> v.setColor(RED));
        FloatBuffer verticesBuffer = buildVerticeBuffer(faceVertices);
        IntBuffer indicesBuffer = buildIndicesBuffer(FACE_INDICES, 6);
        return buildDrawableObject(verticesBuffer, indicesBuffer);
    }

    public static DrawableObject build3x3x3Cubes() {
        List<Vertex> faceVertices = new ArrayList<>();
        faceVertices.addAll(getCubeVectors(-1, 1, 1));
        faceVertices.addAll(getCubeVectors(0, 1, 1));
        faceVertices.addAll(getCubeVectors(1, 1, 1));
        faceVertices.addAll(getCubeVectors(-1, 0, 1));
        faceVertices.addAll(getCubeVectors(0, 0, 1));
        faceVertices.addAll(getCubeVectors(1, 0, 1));
        faceVertices.addAll(getCubeVectors(-1, -1, 1));
        faceVertices.addAll(getCubeVectors(0, -1, 1));
        faceVertices.addAll(getCubeVectors(1, -1, 1));
        faceVertices.addAll(getCubeVectors(-1, 1, 0));
        faceVertices.addAll(getCubeVectors(0, 1, 0));
        faceVertices.addAll(getCubeVectors(1, 1, 0));
        faceVertices.addAll(getCubeVectors(-1, 0, 0));
        faceVertices.addAll(getCubeVectors(0, 0, 0));
        faceVertices.addAll(getCubeVectors(1, 0, 0));
        faceVertices.addAll(getCubeVectors(-1, -1, 0));
        faceVertices.addAll(getCubeVectors(0, -1, 0));
        faceVertices.addAll(getCubeVectors(1, -1, 0));
        faceVertices.addAll(getCubeVectors(-1, 1, -1));
        faceVertices.addAll(getCubeVectors(0, 1, -1));
        faceVertices.addAll(getCubeVectors(1, 1, -1));
        faceVertices.addAll(getCubeVectors(-1, 0, -1));
        faceVertices.addAll(getCubeVectors(0, 0, -1));
        faceVertices.addAll(getCubeVectors(1, 0, -1));
        faceVertices.addAll(getCubeVectors(-1, -1, -1));
        faceVertices.addAll(getCubeVectors(0, -1, -1));
        faceVertices.addAll(getCubeVectors(1, -1, -1));
        faceVertices.stream().forEach(v -> v.setColor(RED));
        FloatBuffer verticesBuffer = buildVerticeBuffer(faceVertices);
        IntBuffer indicesBuffer = buildIndicesBuffer(FACE_INDICES, 6*27);
        return buildDrawableObject(verticesBuffer, indicesBuffer);
    }
}
