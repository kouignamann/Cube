package fr.kouignamann.cube.core.utils;

import org.lwjgl.util.vector.*;

public class MathUtils {

    public static final Vector3f X_AXIS_VECTOR = new Vector3f(1, 0, 0);
    public static final Vector3f Y_AXIS_VECTOR = new Vector3f(0, 1, 0);
    public static final Vector3f Z_AXIS_VECTOR = new Vector3f(0, 0, 1);

    public static float coTangent(float angle) {
        return (float)(1f / Math.tan(angle));
    }
}
