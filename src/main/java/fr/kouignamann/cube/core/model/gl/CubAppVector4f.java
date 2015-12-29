package fr.kouignamann.cube.core.model.gl;

import org.lwjgl.util.vector.*;

import java.util.*;

public class CubAppVector4f extends Vector4f {

    public CubAppVector4f(float[] xyzw) {
        super(xyzw[0], xyzw[1], xyzw[2], xyzw[3]);
    }

    public CubAppVector4f(Vertex vertex) {
        super(vertex.getXyzw()[0], vertex.getXyzw()[1], vertex.getXyzw()[2], vertex.getXyzw()[3]);
    }

    @Override
    public boolean equals(Object obj) {
        CubAppVector4f other = (CubAppVector4f)obj;
        return Objects.equals(this.getX(), other.getX())
                && Objects.equals(this.getY(), other.getY())
                && Objects.equals(this.getZ(), other.getZ())
                && Objects.equals(this.getW(), other.getW());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getX(), this.getY(), this.getZ(), this.getW());
    }

    public CubAppVector4f clone() {
        return new CubAppVector4f(new float[] {this.getX(), this.getY(), this.getZ(), this.getW()});
    }

    public void rotate(Vector4f rotationCenter, float[] xRotationMatrix, float[] yRotationMatrix, float[] zRotationMatrix) {
        Vector4f.sub(this, rotationCenter, this);

        // x rotation
        set(    getX()*xRotationMatrix[0] + getY()*xRotationMatrix[1] + getZ()*xRotationMatrix[2],
                getX()*xRotationMatrix[3] + getY()*xRotationMatrix[4] + getZ()*xRotationMatrix[5],
                getX()*xRotationMatrix[6] + getY()*xRotationMatrix[7] + getZ()*xRotationMatrix[8]);

        // y rotation
        set(    getX()*yRotationMatrix[0] + getY()*yRotationMatrix[1] + getZ()*yRotationMatrix[2],
                getX()*yRotationMatrix[3] + getY()*yRotationMatrix[4] + getZ()*yRotationMatrix[5],
                getX()*yRotationMatrix[6] + getY()*yRotationMatrix[7] + getZ()*yRotationMatrix[8]);

        //z rotation
        set(    getX()*zRotationMatrix[0] + getY()*zRotationMatrix[1] + getZ()*zRotationMatrix[2],
                getX()*zRotationMatrix[3] + getY()*zRotationMatrix[4] + getZ()*zRotationMatrix[5],
                getX()*zRotationMatrix[6] + getY()*zRotationMatrix[7] + getZ()*zRotationMatrix[8]);

        Vector4f.add(this, rotationCenter, this);
    }

    public static List<CubAppVector4f> orderOpposedVertex(List<CubAppVector4f> unorderedVectors) {
        List<CubAppVector4f> orderedVectors = new ArrayList<>();
        while (!unorderedVectors.isEmpty()) {
            CubAppVector4f vectorToCompare = unorderedVectors.remove(1);
            orderedVectors.add(vectorToCompare);
            for (CubAppVector4f vector : unorderedVectors) {
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
