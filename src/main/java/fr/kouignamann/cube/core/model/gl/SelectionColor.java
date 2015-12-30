package fr.kouignamann.cube.core.model.gl;

import java.util.*;

public class SelectionColor {

    private static final int MAX_COLOR_AS_INT = 16777214; // 2^24 - 2

    private static final float COLOR_STEP = 1f / 255f;

    private static long lastSelectionColor = 0l;

    private float[] color;

    private SelectionColor(Long colorAsInt) {
        super();
        float r = colorAsInt%256 * COLOR_STEP;
        float g = (colorAsInt/256)%256 * COLOR_STEP;
        float b = (colorAsInt/65536)%256 * COLOR_STEP;
        this.color = new float[] {r, g, b, 1f};
    }

    public float[] getColor() {
        return color;
    }

    public boolean equals(SelectionColor otherSelectionColor) {
        return Objects.equals(color[0], otherSelectionColor.color[0])
                && Objects.equals(color[1], otherSelectionColor.color[1])
                && Objects.equals(color[2], otherSelectionColor.color[2]);
    }

    @Override
    public String toString() {
        return String.format("{RGBA : %f / %f / %f / %f}", color[0], color[1], color[2], color[3]);
    }

    public static SelectionColor getNextSelectionColor() {
        lastSelectionColor++;

        if (lastSelectionColor > MAX_COLOR_AS_INT) {
            throw new IllegalStateException("Max picking color overstepped");
        }

        return new SelectionColor(lastSelectionColor);
    }
}
