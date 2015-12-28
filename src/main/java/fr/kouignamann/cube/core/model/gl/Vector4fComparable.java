package fr.kouignamann.cube.core.model.gl;

import org.lwjgl.util.vector.*;

import java.util.*;

public class Vector4fComparable extends Vector4f {

    public Vector4fComparable(float[] xyzw) {
        super(xyzw[0], xyzw[1], xyzw[2], xyzw[3]);
    }

    public Vector4fComparable(Vertex vertex) {
        super(vertex.getXyzw()[0], vertex.getXyzw()[1], vertex.getXyzw()[2], vertex.getXyzw()[3]);
    }

    @Override
    public boolean equals(Object obj) {
        Vector4fComparable other = (Vector4fComparable)obj;
        return Objects.equals(this.getX(), other.getX())
                && Objects.equals(this.getY(), other.getY())
                && Objects.equals(this.getZ(), other.getZ())
                && Objects.equals(this.getW(), other.getW());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getX(), this.getY(), this.getZ(), this.getW());
    }

    public Vector4fComparable clone() {
        return new Vector4fComparable(new float[] {this.getX(), this.getY(), this.getZ(), this.getW()});
    }

    public static List<Vector4fComparable> orderOpposedVertex(List<Vector4fComparable> unorderedVectors) {
        List<Vector4fComparable> orderedVectors = new ArrayList<>();
        while (!unorderedVectors.isEmpty()) {
            Vector4fComparable vectorToCompare = unorderedVectors.remove(1);
            orderedVectors.add(vectorToCompare);
            for (Vector4fComparable vector : unorderedVectors) {
                if (vectorToCompare.getX() != vector.getX()
                        && vectorToCompare.getY() != vector.getY()
                        && vectorToCompare.getZ() != vector.getZ()) {
                    orderedVectors.add(vector);
                    unorderedVectors.remove(vector);
                    break;
                }
            }
        }
        return orderedVectors;
    }
}
