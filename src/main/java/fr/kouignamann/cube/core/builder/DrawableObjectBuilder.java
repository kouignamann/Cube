package fr.kouignamann.cube.core.builder;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import fr.kouignamann.cube.core.CubeAppTextures;
import fr.kouignamann.cube.core.model.drawable.DrawableObject;
import fr.kouignamann.cube.core.model.drawable.DrawableObjectPart;
import fr.kouignamann.cube.core.model.gl.Vertex;
import fr.kouignamann.cube.core.utils.GlUtils;

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

    protected static List<DrawableObjectPart> newSingleDrawableObjectPartAsList(int length) {
        List<DrawableObjectPart> results = new ArrayList<>();
        results.add(new DrawableObjectPart(0, length));
        return results;
    }

    protected static DrawableObject buildDrawableObject(FloatBuffer verticesBuffer, IntBuffer indicesBuffer, List<DrawableObjectPart> parts, String textureName) {
        int vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
        int vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_DYNAMIC_DRAW);
        GL20.glVertexAttribPointer(0, Vertex.POSITION_ELEMENT_COUNT, GL11.GL_FLOAT, false, Vertex.STRIDE, 0);
        GL20.glVertexAttribPointer(1, Vertex.COLOR_ELEMENT_COUNT, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.COLOR_BYTE_OFFSET);
        GL20.glVertexAttribPointer(2, Vertex.SELECT_COLOR_ELEMENT_COUNT, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.SELECT_COLOR_BYTE_OFFSET);
        GL20.glVertexAttribPointer(3, Vertex.TEXTURE_ELEMENT_COUNT, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.TEXTURE_BYTE_OFFSET);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        int vboiId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);

        int textureId = CubeAppTextures.getTextureId(textureName);

        GlUtils.exitOnGLError("Failed drawable object build");

        return new DrawableObject(vaoId, vboId, vboiId, indicesBuffer.limit(), textureId, verticesBuffer, parts);
    }
}
