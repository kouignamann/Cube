/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.kouignamann.cube.core.model.gl;

import org.lwjgl.util.vector.Vector3f;

public class Vertex {
    
    // DATA
    private float[] xyzw = new float[] {0f, 0f, 0f, 1f};

    //VERTEX CONST
    // The amount of bytes an element has
    public static final int ELEMENT_BYTES = 4;
    
    // Elements per parameter
    public static final int POSITION_ELEMENT_COUNT = 4;

    // Bytes per parameter
    public static final int POSITION_BYTES_COUNT = POSITION_ELEMENT_COUNT * ELEMENT_BYTES;

    // Byte offsets per parameter
    public static final int POSITION_BYTE_OFFSET = 0;

    // The amount of elements that a vertex has
    public static final int ELEMENT_COUNT = POSITION_ELEMENT_COUNT;
    public static final int STRIDE = POSITION_BYTES_COUNT;
    
    public float[] getElements() {
        return new float[] {
            xyzw[0], xyzw[1], xyzw[2], xyzw[3]
        };
    }
    
	public Vertex setPosition(Vector3f vector3f) {
        this.xyzw = new float[] {vector3f.getX(),vector3f.getY(), vector3f.getZ(), 1.0f};
        return this;
    }
}
