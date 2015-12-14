package fr.kouignamann.cube.core.builder;

import fr.kouignamann.cube.core.model.drawable.DrawableObject;
import fr.kouignamann.cube.core.model.gl.Vertex;
import fr.kouignamann.cube.core.utils.GlUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static fr.kouignamann.cube.core.Constant.ORANGE;

public class FaceBuilder {

    private static final Vector3f CUBE_FACE_POINT_0 = new Vector3f(-0.5f,   0.5f,   0f);    // Left top
    private static final Vector3f CUBE_FACE_POINT_1 = new Vector3f(-0.5f,   -0.5f,  0f);    // Left bottom
    private static final Vector3f CUBE_FACE_POINT_2 = new Vector3f(0.5f,    -0.5f,  0f);    // Right bottom
    private static final Vector3f CUBE_FACE_POINT_3 = new Vector3f(0.5f,    0.5f,   0f);    // Right top

    private final static int[] FACE_INDICES = {
            0,	1,	2,
            2,	3,	0
    };

    public static DrawableObject buildFace() {
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(Vertex.ELEMENT_COUNT * 4);
        verticesBuffer.put(new Vertex().setPosition(CUBE_FACE_POINT_0).setColor(ORANGE).setSt(new float[]{0f, 0f}).getElements());
        verticesBuffer.put(new Vertex().setPosition(CUBE_FACE_POINT_1).setColor(ORANGE).setSt(new float[]{0f, 1f}).getElements());
        verticesBuffer.put(new Vertex().setPosition(CUBE_FACE_POINT_2).setColor(ORANGE).setSt(new float[]{1f, 1f}).getElements());
        verticesBuffer.put(new Vertex().setPosition(CUBE_FACE_POINT_3).setColor(ORANGE).setSt(new float[]{1f, 0f}).getElements());
        verticesBuffer.flip();

        int vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
        int vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, Vertex.POSITION_ELEMENT_COUNT, GL11.GL_FLOAT, false, Vertex.STRIDE, 0);
        GL20.glVertexAttribPointer(1, Vertex.COLOR_ELEMENT_COUNT, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.COLOR_BYTE_OFFSET);
        GL20.glVertexAttribPointer(2, Vertex.TEXTURE_ELEMENT_COUNT, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.TEXTURE_BYTE_OFFSET);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(FACE_INDICES.length);
        indicesBuffer.put(FACE_INDICES);
        indicesBuffer.flip();
        int vboiId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);

        GlUtils.exitOnGLError("Failed face build");

        return new DrawableObject(vaoId, vboId, vboiId, indicesBuffer.limit());
    }
}
