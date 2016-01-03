package fr.kouignamann.cube.core.model.drawable;

import fr.kouignamann.cube.core.model.gl.*;
import fr.kouignamann.cube.core.utils.*;
import org.lwjgl.util.vector.*;

import java.util.*;

import static fr.kouignamann.cube.core.utils.MathUtils.RotationAxis.*;

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
    private float[] xRotationMatrix;
    private float[] yRotationMatrix;
    private float[] zRotationMatrix;

    public DrawableObjectPart(int startIndex, int length, boolean selectable) {
        this.startIndex = startIndex;
        this.length = length;
        this.nbVertex = 4*length/6;
        this.startVertexIndex = 4*startIndex/6;
        this.selectionColor = selectable ? SelectionColor.getNextSelectionColor() : SelectionColor.NOTHING;
        this.nbElmements = nbVertex*Vertex.ELEMENT_COUNT;
        this.xRotationMatrix = MathUtils.computeRotationMatrix(rotation.getX(), X_AXIS, null);
        this.yRotationMatrix = MathUtils.computeRotationMatrix(rotation.getY(), Y_AXIS, null);
        this.zRotationMatrix = MathUtils.computeRotationMatrix(rotation.getZ(), Z_AXIS, null);
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
        return xRotationMatrix;
    }

    public float[] getyRotationMatrix() {
        return yRotationMatrix;
    }

    public float[] getzRotationMatrix() {
        return zRotationMatrix;
    }

    public void rotateX(float xRotation) {
        rotation.x += xRotation;
        MathUtils.computeRotationMatrix(rotation.getX(), X_AXIS, xRotationMatrix);
    }

    public void rotateY(float yRotation) {
        rotation.y += yRotation;
        MathUtils.computeRotationMatrix(rotation.getY(), Y_AXIS, yRotationMatrix);
    }

    public void rotateZ(float zRotation) {
        rotation.z += zRotation;
        MathUtils.computeRotationMatrix(rotation.getZ(), Z_AXIS, zRotationMatrix);
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
    public DrawableObject getParent() {
        return parent;
    }
}
