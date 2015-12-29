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

    public static float[] computeRotationAnimationMatrix(Vector3f rotation, RotationAxis axis) {
        switch (axis) {
            case X_AXIS:
                // MATRIX : X ROTATION
                return new float[] {
                        1.0f, 0.0f, 0.0f,
                        0.0f, (float)Math.cos(rotation.getX()), -(float)Math.sin(rotation.getX()),
                        0.0f, (float)Math.sin(rotation.getX()), (float)Math.cos(rotation.getX())};
            case Y_AXIS:
                // MATRIX : Y ROTATION
                return new float[] {
                        (float)Math.cos(rotation.getY()), 0.0f, (float)Math.sin(rotation.getY()),
                        0.0f, 1.0f, 0.0f,
                        -(float)Math.sin(rotation.getY()), 0.0f, (float)Math.cos(rotation.getY())};
            case Z_AXIS:
                // MATRIX : Z ROTATION
                return new float[] {
                        (float)Math.cos(rotation.getZ()), -(float)Math.sin(rotation.getZ()), 0.0f,
                        (float)Math.sin(rotation.getZ()), (float)Math.cos(rotation.getZ()), 0.0f,
                        0.0f, 0.0f, 1.0f};
            default:
                // IDENTITY MATRIX (no rotation)
                return new float[] {
                        1.0f, 0.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 0.0f, 1.0f};
        }
    }
}
