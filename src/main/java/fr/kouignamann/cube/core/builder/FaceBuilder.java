package fr.kouignamann.cube.core.builder;

import static fr.kouignamann.cube.core.Constant.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.kouignamann.cube.core.CubeAppTextures;
import fr.kouignamann.cube.core.model.drawable.DrawableObject;
import fr.kouignamann.cube.core.model.gl.Vertex;

public class FaceBuilder extends DrawableObjectBuilder {

    private final static float FACE_WIDTH = 100f;
    private final static float FACE_MARGIN = 1f;
    private final static float FACE_REAL_WIDTH = FACE_WIDTH - FACE_MARGIN;
    private final static float CUBE_UNIT = FACE_REAL_WIDTH / 2.0f;

    private final static int[] FACE_INDICES = {
            0,	1,	2,
            2,	3,	0
    };

    private static List<Vertex> getFaceVectors(int abs, int ord) {
        return Arrays.asList(
                new Vertex(-CUBE_UNIT,   CUBE_UNIT,   0f).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, 0).setSt(0f, 0f),   // Left top
                new Vertex(-CUBE_UNIT,   -CUBE_UNIT,  0f).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, 0).setSt(0f, 1f),   // Left bottom
                new Vertex(CUBE_UNIT,    -CUBE_UNIT,  0f).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, 0).setSt(1f, 1f),   // Right bottom
                new Vertex(CUBE_UNIT,    CUBE_UNIT,   0f).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, 0).setSt(1f, 0f)    // Right top
        );
    }

    public static DrawableObject buildFace() {
        List<Vertex> faceVertices = getFaceVectors(0, 0);
        faceVertices.stream().forEach(v -> v.setColor(RED));
        FloatBuffer verticesBuffer = buildVerticeBuffer(faceVertices);
        IntBuffer indicesBuffer = buildIndicesBuffer(FACE_INDICES, 1);

        return buildDrawableObject(verticesBuffer, indicesBuffer, newSingleDrawableObjectPartAsList(FACE_INDICES.length), CubeAppTextures.CUBE_TEXTURE_NAME);
    }

    public static DrawableObject build3x3Faces() {
        List<Vertex> faceVertices = new ArrayList<>();
        faceVertices.addAll(getFaceVectors(-1, 1));
        faceVertices.addAll(getFaceVectors(0, 1));
        faceVertices.addAll(getFaceVectors(1, 1));
        faceVertices.addAll(getFaceVectors(-1, 0));
        faceVertices.addAll(getFaceVectors(0, 0));
        faceVertices.addAll(getFaceVectors(1, 0));
        faceVertices.addAll(getFaceVectors(-1, -1));
        faceVertices.addAll(getFaceVectors(0, -1));
        faceVertices.addAll(getFaceVectors(1, -1));
        faceVertices.stream().forEach(v -> v.setColor(RED));
        FloatBuffer verticesBuffer = buildVerticeBuffer(faceVertices);
        IntBuffer indicesBuffer = buildIndicesBuffer(FACE_INDICES, 9);
        return buildDrawableObject(verticesBuffer, indicesBuffer, newSingleDrawableObjectPartAsList(FACE_INDICES.length*9), CubeAppTextures.CUBE_TEXTURE_NAME);
    }
}
