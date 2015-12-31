package fr.kouignamann.cube.core.model.drawable;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import fr.kouignamann.cube.core.model.gl.SelectionColor;

public class DrawableObject {
	
	private int vaoId;
	private int vboId;
	private int vboiId;
	private int nbIndices;

	private FloatBuffer verticeBuffer;

	private List<DrawableObjectPart> parts;

	private int textureId;
	
	public DrawableObject(int vaoId, int vboId, int vboiId, int nbIndices, int textureId,
						  FloatBuffer verticeBuffer, List<DrawableObjectPart> parts) {
		super();
		this.vaoId = vaoId;
		this.vboId = vboId;
		this.vboiId = vboiId;
		this.nbIndices = nbIndices;
		this.textureId = textureId;
		this.verticeBuffer = verticeBuffer;
		this.parts = parts;
		if (this.parts == null) {
			this.parts = new ArrayList<>();
			this.parts.add(new DrawableObjectPart(0, nbIndices));
		}
		parts.stream().forEach(p -> p.parent = this);
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

	public DrawableObjectPart findDrawableObjectPartByVertexIndex(int index) {
		for (DrawableObjectPart part : parts) {
			if (index>=part.getStartIndex() && index<=part.getLastIndex()) {
				return part;
			}
		}
		throw new IllegalStateException(
				String.format("Drawable object part not found (by index) : size = %d, index = %d", nbIndices, index));
	}

	public DrawableObjectPart findDrawableObjectPartBySelectionColor(SelectionColor selectionColor) {
		for (DrawableObjectPart part : parts) {
			if (part.getSelectionColor().equals(selectionColor)) {
				return part;
			}
		}
		throw new IllegalStateException(
				String.format("Drawable object part not found (by selection color) : color = %s", selectionColor));
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
	public int getTextureId() {
		return textureId;
	}
	public FloatBuffer getVerticeBuffer() {
		return verticeBuffer;
	}
	public List<DrawableObjectPart> getParts() {
		return parts;
	}
}
