package fr.kouignamann.cube.core.model.drawable;

import static fr.kouignamann.cube.core.utils.MathUtils.RotationAxis.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import fr.kouignamann.cube.core.model.gl.SelectionColor;
import fr.kouignamann.cube.core.model.gl.Vertex;
import fr.kouignamann.cube.core.utils.MathUtils;

public class DrawableObjectPart {

    private int startIndex;
    private int length;
    private SelectionColor selectionColor;
    DrawableObject parent;
    private Vector3f rotation = new Vector3f(0.0f, 0.0f, 0.0f);
    private float scale = 100.0f;

    // Computed elements
    private int nbVertex;
    private int startVertexIndex;
    private int nbElmements;

    public DrawableObjectPart(int startIndex, int length) {
        this.startIndex = startIndex;
        this.length = length;
        this.nbVertex = 4*length/6;
        this.startVertexIndex = 4*startIndex/6;
        this.selectionColor = SelectionColor.getNextSelectionColor();
        this.nbElmements = nbVertex*Vertex.ELEMENT_COUNT;
    }

    public List<Vertex> readVertice() {
        parent.getVerticeBuffer().position(startVertexIndex*Vertex.ELEMENT_COUNT);
        List<Vertex> results = new ArrayList<>(6);
        for (int i = nbVertex; i > 0; i--) {
            results.add(Vertex.readVertex(parent.getVerticeBuffer()));
        }
        parent.getVerticeBuffer().rewind();
        return results;
    }

    public void pushVertice(List<Vertex> vertice) {
        parent.getVerticeBuffer().position(startVertexIndex*Vertex.ELEMENT_COUNT);
        for (Vertex vertex : vertice) {
            parent.getVerticeBuffer().put(vertex.getElements());
        }
        parent.getVerticeBuffer().rewind();
    }

    public float getRealScale() {
        return 0.5f-(scale/200f);
    }

    public float[] getxRotationMatrix() {
        return MathUtils.computeRotationAnimationMatrix(rotation, X_AXIS);
    }

    public float[] getyRotationMatrix() {
        return MathUtils.computeRotationAnimationMatrix(rotation, Y_AXIS);
    }

    public float[] getzRotationMatrix() {
        return MathUtils.computeRotationAnimationMatrix(rotation, Z_AXIS);
    }

    public int getStartIndex() {
        return startIndex;
    }
    public int getLastIndex() {
        return startIndex+length-1;
    }
    public int getLength() {
        return length;
    }
    public SelectionColor getSelectionColor() {
        return selectionColor;
    }
    public int getStartVertexIndex() {
        return startVertexIndex;
    }
    public int getLastVertexIndex() {
        return startVertexIndex+nbVertex-1;
    }
    public int getNbVertex() {
        return nbVertex;
    }
    public float getScale() {
        return scale;
    }
    public void setScale(float scale) {
        this.scale = scale;
    }
    public Vector3f getRotation() {
        return rotation;
    }
    public DrawableObject getParent() {
        return parent;
    }
}
