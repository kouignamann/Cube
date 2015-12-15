package fr.kouignamann.cube.core.builder;

import fr.kouignamann.cube.core.model.drawable.DrawableObject;
import fr.kouignamann.cube.core.model.gl.Vertex;
import fr.kouignamann.cube.core.utils.GlUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fr.kouignamann.cube.core.Constant.RED;

public class FaceBuilder {

    private final static float FACE_WIDTH = 0.4f;
    private final static float FACE_MARGIN = 0.02f;
    private final static float FACE_REAL_WIDTH = FACE_WIDTH - FACE_MARGIN;
    private final static float CUBE_UNIT = FACE_REAL_WIDTH / 2.0f;

    private final static int[] FACE_INDICES = {
            0,	1,	2,
            2,	3,	0
    };

    private static List<Vertex> getFaceVectors(int abs, int ord) {
        return Arrays.asList(new Vertex(-CUBE_UNIT,   CUBE_UNIT,   0f).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, 0).setSt(0f, 0f),   // Left top
                new Vertex(-CUBE_UNIT,   -CUBE_UNIT,  0f).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, 0).setSt(0f, 1f),   // Left bottom
                new Vertex(CUBE_UNIT,    -CUBE_UNIT,  0f).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, 0).setSt(1f, 1f),   // Right bottom
                new Vertex(CUBE_UNIT,    CUBE_UNIT,   0f).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, 0).setSt(1f, 0f)    // Right top
        );
    }

    private static FloatBuffer buildVerticeBuffer(List<Vertex> vertices) {
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(Vertex.ELEMENT_COUNT * vertices.size());
        for (Vertex vertex : vertices) {
            verticesBuffer.put(vertex.getElements());
        }
        verticesBuffer.flip();
        return verticesBuffer;
    }

    private static IntBuffer buildIndicesBuffer(int nbFaces) {
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(FACE_INDICES.length*nbFaces);
        for (int i = 0; i < nbFaces; i++) {
            int offset = i*4;
            indicesBuffer.put(new int[] {
                    FACE_INDICES[0]+offset, FACE_INDICES[1]+offset, FACE_INDICES[2]+offset,
                    FACE_INDICES[3]+offset, FACE_INDICES[4]+offset, FACE_INDICES[5]+offset
            });
        }
        indicesBuffer.flip();
        return indicesBuffer;
    }

    private static DrawableObject buildDrawableObject(FloatBuffer verticesBuffer, IntBuffer indicesBuffer) {
        int vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
        int vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, Vertex.POSITION_ELEMENT_COUNT, GL11.GL_FLOAT, false, Vertex.STRIDE, 0);
        GL20.glVertexAttribPointer(1, Vertex.COLOR_ELEMENT_COUNT, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.COLOR_BYTE_OFFSET);
        GL20.glVertexAttribPointer(2, Vertex.TEXTURE_ELEMENT_COUNT, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.TEXTURE_BYTE_OFFSET);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        int vboiId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);

        GlUtils.exitOnGLError("Failed face build");

        return new DrawableObject(vaoId, vboId, vboiId, indicesBuffer.limit());
    }

    public static DrawableObject buildFace() {
        List<Vertex> faceVertices = getFaceVectors(0, 0);
        faceVertices.stream().forEach(v -> v.setColor(RED));
        FloatBuffer verticesBuffer = buildVerticeBuffer(faceVertices);
        IntBuffer indicesBuffer = buildIndicesBuffer(faceVertices.size());
        return buildDrawableObject(verticesBuffer, indicesBuffer);
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
        IntBuffer indicesBuffer = buildIndicesBuffer(faceVertices.size());
        return buildDrawableObject(verticesBuffer, indicesBuffer);
    }
}
