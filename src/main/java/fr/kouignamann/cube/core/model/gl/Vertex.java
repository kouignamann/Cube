/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.kouignamann.cube.core.model.gl;

import org.lwjgl.util.vector.*;

import java.nio.*;

public class Vertex {
    
    // DATA
    private float[] xyzw = new float[] {0f, 0f, 0f, 1f};
    private float[] rgba = new float[] {1f, 1f, 1f, 1f};
    private float[] selectRgba = new float[] {1f, 1f, 1f, 1f};
    private float[] st = new float[] {1f, 1f};

    //VERTEX CONST
    // The amount of bytes an element has
    public static final int ELEMENT_BYTES = 4;
    
    // Elements per parameter
    public static final int POSITION_ELEMENT_COUNT = 4;
    public static final int COLOR_ELEMENT_COUNT = 4;
    public static final int SELECT_COLOR_ELEMENT_COUNT = 4;
    public static final int TEXTURE_ELEMENT_COUNT = 2;

    // Bytes per parameter
    public static final int POSITION_BYTES_COUNT = POSITION_ELEMENT_COUNT * ELEMENT_BYTES;
    public static final int COLOR_BYTE_COUNT = COLOR_ELEMENT_COUNT * ELEMENT_BYTES;
    public static final int SELECT_COLOR_BYTE_COUNT = SELECT_COLOR_ELEMENT_COUNT * ELEMENT_BYTES;
    public static final int TEXTURE_BYTE_COUNT = TEXTURE_ELEMENT_COUNT * ELEMENT_BYTES;

    // Byte offsets per parameter
    public static final int POSITION_BYTE_OFFSET = 0;
    public static final int COLOR_BYTE_OFFSET = POSITION_BYTE_OFFSET + POSITION_BYTES_COUNT;
    public static final int SELECT_COLOR_BYTE_OFFSET = COLOR_BYTE_OFFSET + COLOR_BYTE_COUNT;
    public static final int TEXTURE_BYTE_OFFSET = SELECT_COLOR_BYTE_OFFSET + SELECT_COLOR_BYTE_COUNT;

    // The amount of elements that a vertex has
    public static final int ELEMENT_COUNT =
            POSITION_ELEMENT_COUNT +
            COLOR_ELEMENT_COUNT +
            SELECT_COLOR_ELEMENT_COUNT +
            TEXTURE_ELEMENT_COUNT;
    public static final int STRIDE =
            POSITION_BYTES_COUNT +
            COLOR_BYTE_COUNT +
            SELECT_COLOR_BYTE_COUNT +
            TEXTURE_BYTE_COUNT;

    public Vertex(float x, float y, float z) {
        xyzw[0] = x;
        xyzw[1] = y;
        xyzw[2] = z;
    }

    private Vertex() {
        this.xyzw = new float[4];
        this.rgba = new float[4];
        this.st = new float[2];
    }

    public static Vertex readVertex(FloatBuffer verticeBuffer) {
        Vertex result = new Vertex();
        verticeBuffer.get(result.xyzw);
        verticeBuffer.get(result.rgba);
        verticeBuffer.get(result.selectRgba);
        verticeBuffer.get(result.st);
        return result;
    }

    public Vertex translate(float x, float y, float z) {
        xyzw[0] += x;
        xyzw[1] += y;
        xyzw[2] += z;
        return this;
    }
    
    public float[] getElements() {
        return new float[]{
                xyzw[0], xyzw[1], xyzw[2], xyzw[3],
                rgba[0], rgba[1], rgba[2], rgba[3],
                selectRgba[0], selectRgba[1], selectRgba[2], selectRgba[3],
                st[0], st[1]
        };
    }
    
	public Vertex setPosition(Vector4f vector4f) {
        this.xyzw = new float[] {vector4f.getX(),vector4f.getY(), vector4f.getZ(), vector4f.getW()};
        return this;
    }

    public Vertex setColor(float[] rgba) {
        this.rgba = new float[] {rgba[0], rgba[1], rgba[2], rgba[3]};
        return this;
    }

    public Vertex setSelectColor(float[] selectRgba) {
        this.selectRgba = new float[] {selectRgba[0], selectRgba[1], selectRgba[2], selectRgba[3]};
        return this;
    }

    public Vertex setSt(float s, float t) {
        this.st[0] = s;
        this.st[1] = t;
        return this;
    }

    public float[] getXyzw() {
        return xyzw;
    }
}
