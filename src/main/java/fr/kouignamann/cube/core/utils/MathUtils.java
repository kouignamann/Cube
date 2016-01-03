package fr.kouignamann.cube.core.utils;

import org.lwjgl.util.vector.*;

public class MathUtils {

    public enum RotationAxis {
        X_AXIS(new Vector3f(1, 0, 0)),
        Y_AXIS(new Vector3f(0, 1, 0)),
        Z_AXIS(new Vector3f(0, 0, 1));
        public Vector3f vector;
        RotationAxis(Vector3f vector) {
            this.vector = vector;
        }
    }

    public static float coTangent(float angle) {
        return (float)(1f / Math.tan(angle));
    }

    public static float[] computeRotationMatrix(float angle, RotationAxis axis, float[] dest) {
        if (dest == null) {
            dest = new float[9];
        }
        switch (axis) {
            case X_AXIS:
                // MATRIX : X ROTATION
                dest[0] = 1f;     dest[1] = 0f;                         dest[2] = 0f;
                dest[3] = 0f;     dest[4] = (float)Math.cos(angle);     dest[5] = -(float)Math.sin(angle);
                dest[6] = 0f;     dest[7] = (float)Math.sin(angle);     dest[8] = (float)Math.cos(angle);
                return dest;
            case Y_AXIS:
                // MATRIX : Y ROTATION
                dest[0] = (float)Math.cos(angle);   dest[1] = 0f;   dest[2] = (float)Math.sin(angle);
                dest[3] = 0f;                       dest[4] = 1f;   dest[5] = 0f;
                dest[6] = -(float)Math.sin(angle);  dest[7] = 0f;   dest[8] = (float)Math.cos(angle);
                return dest;
            case Z_AXIS:
                // MATRIX : Z ROTATION
                dest[0] = (float)Math.cos(angle);   dest[1] = -(float)Math.sin(angle);  dest[2] = 0f;
                dest[3] = (float)Math.sin(angle);   dest[4] = (float)Math.cos(angle);   dest[5] = 0f;
                dest[6] = 0f;                       dest[7] = 0f;                       dest[8] = 1f;
                return dest;
            default:
                // IDENTITY MATRIX (no rotation)
                dest[0] = 1f;   dest[1] = 0f;   dest[2] = 0f;
                dest[3] = 0f;   dest[4] = 1f;   dest[5] = 0f;
                dest[6] = 0f;   dest[7] = 0f;   dest[8] = 1f;
                return dest;
        }
    }
}
