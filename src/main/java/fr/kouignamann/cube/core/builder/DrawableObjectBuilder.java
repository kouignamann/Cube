package fr.kouignamann.cube.core.builder;

import fr.kouignamann.cube.core.model.drawable.*;
import fr.kouignamann.cube.core.model.gl.*;
import fr.kouignamann.cube.core.utils.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;

import java.nio.*;
import java.util.*;

public abstract class DrawableObjectBuilder {

    protected static FloatBuffer buildVerticeBuffer(List<Vertex> vertices) {
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(Vertex.ELEMENT_COUNT * vertices.size());
        for (Vertex vertex : vertices) {
            verticesBuffer.put(vertex.getElements());
        }
        verticesBuffer.flip();
        return verticesBuffer;
    }

    protected static IntBuffer buildIndicesBuffer(int[] indices, int nbObjects) {
        int nbVertex = new Long(Arrays.stream(indices).distinct().count()).intValue();
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length * nbObjects);
        for (int objectOffset = 0; objectOffset < nbObjects; objectOffset++) {
            int offset = objectOffset*nbVertex;
            for (int index = 0; index < indices.length; index++) {
                indicesBuffer.put(indices[index] + offset);
            }
        }
        indicesBuffer.flip();
        return indicesBuffer;
    }

    protected static DrawableObject buildDrawableObject(FloatBuffer verticesBuffer, IntBuffer indicesBuffer, List<DrawableObjectPart> parts) {
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
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_DYNAMIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);

        GlUtils.exitOnGLError("Failed face build");

        return new DrawableObject(vaoId, vboId, vboiId, indicesBuffer.limit(), verticesBuffer, parts);
    }
}
