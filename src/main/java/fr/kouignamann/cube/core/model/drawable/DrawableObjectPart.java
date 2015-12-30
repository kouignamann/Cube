package fr.kouignamann.cube.core.model.drawable;

import fr.kouignamann.cube.core.model.gl.*;

public class DrawableObjectPart {

    private int startIndex;
    private int length;
    private int startVertexIndex;
    private int nbVertex;
    private SelectionColor selectionColor;

    public DrawableObjectPart(int startIndex, int length) {
        this.startIndex = startIndex;
        this.length = length;
        this.nbVertex = 4*length/6;
        this.startVertexIndex = 4*startIndex/6;
        this.selectionColor = SelectionColor.getNextSelectionColor();
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
}
