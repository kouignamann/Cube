package fr.kouignamann.cube.core.model.gl;

import java.nio.*;

public class SelectionColor {

    public static SelectionColor NOTHING = new SelectionColor(0l);

    private static final int MAX_COLOR_AS_INT = 16777214; // 2^24 - 2

    private static final float MAX_COLOR_TONE_VALUE = 255f;

    private static final float GL_COLOR_STEP = 1f / MAX_COLOR_TONE_VALUE;

    private static long lastSelectionColor = 0l;

    private float[] colorRGBA;

    private long longColorRGB;

    private SelectionColor(long longColorRGB) {
        super();
        this.longColorRGB = longColorRGB;
        float r = longColorRGB%256 * GL_COLOR_STEP;
        float g = (longColorRGB/256)%256 * GL_COLOR_STEP;
        float b = (longColorRGB/65536)%256 * GL_COLOR_STEP;
        this.colorRGBA = new float[] {r, g, b, 1f};
    }

    private SelectionColor(FloatBuffer floatBuffer) {
        super();
        this.colorRGBA = new float[4];
        floatBuffer.get(this.colorRGBA);
        long r = new Float(colorRGBA[0] * MAX_COLOR_TONE_VALUE).longValue();
        long g = new Float(colorRGBA[1] * MAX_COLOR_TONE_VALUE).longValue();
        long b = new Float(colorRGBA[2] * MAX_COLOR_TONE_VALUE).longValue();
        this.longColorRGB = r + g*256 + b*65536;
    }

    public float[] getColorRGBA() {
        return colorRGBA;
    }

    public boolean nothingSelected() {
        return NOTHING.equalsRGB(this);
    }

    public boolean equalsRGB(SelectionColor otherColor) {
        return longColorRGB == otherColor.longColorRGB;
    }

    @Override
    public String toString() {
        return String.format("{RGBA : %f / %f / %f / %f - long RGB : %d}",
                colorRGBA[0], colorRGBA[1], colorRGBA[2], colorRGBA[3], longColorRGB);
    }

    public static SelectionColor getNextSelectionColor() {
        lastSelectionColor++;

        if (lastSelectionColor > MAX_COLOR_AS_INT) {
            throw new IllegalStateException("Max picking colorRGBA overstepped");
        }

        return new SelectionColor(lastSelectionColor);
    }

    public static SelectionColor fromFloatBuffer(FloatBuffer floatBuffer) {
        return new SelectionColor(floatBuffer);
    }
}
