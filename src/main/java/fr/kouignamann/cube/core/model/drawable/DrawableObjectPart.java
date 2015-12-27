package fr.kouignamann.cube.core.model.drawable;

public class DrawableObjectPart {

    private int startIndex;
    private int length;

    public DrawableObjectPart(int startIndex, int length) {
        this.startIndex = startIndex;
        this.length = length;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getLength() {
        return length;
    }
}
