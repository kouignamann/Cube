package fr.kouignamann.cube.core.model.drawable;

import org.lwjgl.opengl.*;

import java.nio.*;

public class DrawableObject {
	
	private int vaoId;
	private int vboId;
	private int vboiId;
	private int nbIndices;

	private FloatBuffer verticeBuffer;
	
	public DrawableObject(int vaoId, int vboId, int vboiId, int nbIndices, FloatBuffer verticeBuffer) {
		super();
		this.vaoId = vaoId;
		this.vboId = vboId;
		this.vboiId = vboiId;
		this.nbIndices = nbIndices;
		this.verticeBuffer = verticeBuffer;
	}
	
	public void destroy() {
		GL30.glBindVertexArray(vaoId);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vboId);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vboiId);
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoId);
	}
	
	public int getVaoId() {
		return vaoId;
	}
	public int getVboId() {
		return vboId;
	}
	public int getVboiId() {
		return vboiId;
	}
	public int getNbIndices() {
		return nbIndices;
	}
}
