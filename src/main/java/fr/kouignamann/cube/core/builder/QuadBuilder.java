package fr.kouignamann.cube.core.builder;

import fr.kouignamann.cube.core.*;
import fr.kouignamann.cube.core.model.drawable.*;
import fr.kouignamann.cube.core.model.gl.*;

import java.nio.*;
import java.util.*;

import static fr.kouignamann.cube.core.Constant.*;

public class QuadBuilder extends DrawableObjectBuilder {

    private final static float FACE_WIDTH = 100f;
    private final static float CUBE_WIDTH = 400f;

    private final static float FACE_MARGIN = 1f;
    private final static float FACE_REAL_WIDTH = FACE_WIDTH - FACE_MARGIN;
    private final static float CUBE_UNIT = FACE_REAL_WIDTH / 2.0f;
    private final static float GROUND_UNIT = CUBE_WIDTH / 2.0f;

    private final static float GROUND_Y_VALUE = -200f;

    private final static int[] QUAD_INDICES = {
            0,	1,	2,
            2,	3,	0
    };

    private static List<Vertex> getFaceVectors(int abs, int ord) {
        return Arrays.asList(
                new Vertex(-CUBE_UNIT,   CUBE_UNIT,   0f).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, 0).setSt(0f, 0f),   // Left top
                new Vertex(-CUBE_UNIT,   -CUBE_UNIT,  0f).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, 0).setSt(0f, 1f),   // Left bottom
                new Vertex(CUBE_UNIT,    -CUBE_UNIT,  0f).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, 0).setSt(1f, 1f),   // Right bottom
                new Vertex(CUBE_UNIT,    CUBE_UNIT,   0f).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, 0).setSt(1f, 0f)    // Right top
        );
    }

    private static List<Vertex> getGroundTileVectors(int abs, int ord) {
        return Arrays.asList(
                new Vertex(-GROUND_UNIT,   GROUND_Y_VALUE,   GROUND_UNIT).translate(abs*CUBE_WIDTH, 0, ord*CUBE_WIDTH).setSt(0f, 0f),   // Left top
                new Vertex(-GROUND_UNIT,   GROUND_Y_VALUE,  -GROUND_UNIT).translate(abs*CUBE_WIDTH, 0, ord*CUBE_WIDTH).setSt(0f, 1f),   // Left bottom
                new Vertex(GROUND_UNIT,    GROUND_Y_VALUE,  -GROUND_UNIT).translate(abs*CUBE_WIDTH, 0, ord*CUBE_WIDTH).setSt(1f, 1f),   // Right bottom
                new Vertex(GROUND_UNIT,    GROUND_Y_VALUE,   GROUND_UNIT).translate(abs*CUBE_WIDTH, 0, ord*CUBE_WIDTH).setSt(1f, 0f)    // Right top
        );
    }

    public static DrawableObject buildFace() {
        List<Vertex> faceVertices = getFaceVectors(0, 0);
        faceVertices.stream().forEach(v -> v.setColor(RED));
        FloatBuffer verticesBuffer = buildVerticeBuffer(faceVertices);
        IntBuffer indicesBuffer = buildIndicesBuffer(QUAD_INDICES, 1);

        return buildDrawableObject(verticesBuffer, indicesBuffer, newSingleDrawableObjectPartAsList(QUAD_INDICES.length, false), CubeAppTextures.CUBE_TEXTURE_NAME, null);
    }

    public static DrawableObject buildGroundTile() {
        List<Vertex> groundTileVertice = getGroundTileVectors(0, 0);
        groundTileVertice.stream().forEach(v -> v.setColor(WHITE));
        groundTileVertice.stream().forEach(v -> v.setSelectColor(SelectionColor.NOTHING.getColorRGBA()));
        FloatBuffer verticesBuffer = buildVerticeBuffer(groundTileVertice);
        IntBuffer indicesBuffer = buildIndicesBuffer(QUAD_INDICES, 1);

        return buildDrawableObject(verticesBuffer, indicesBuffer, newSingleDrawableObjectPartAsList(QUAD_INDICES.length, false), CubeAppTextures.GROUND_TEXTURE_NAME, null);
    }

    public static DrawableObject buildGround() {
        List<Vertex> groundTilesVertice = new ArrayList<>();
        groundTilesVertice.addAll(getGroundTileVectors(0, 0));
        groundTilesVertice.addAll(getGroundTileVectors(1, 0));
        groundTilesVertice.addAll(getGroundTileVectors(0, 1));
        groundTilesVertice.addAll(getGroundTileVectors(1, 1));
        groundTilesVertice.addAll(getGroundTileVectors(-1, 0));
        groundTilesVertice.addAll(getGroundTileVectors(0, -1));
        groundTilesVertice.addAll(getGroundTileVectors(0, -1));
        groundTilesVertice.addAll(getGroundTileVectors(-1, -1));
        groundTilesVertice.addAll(getGroundTileVectors(-1, 1));
        groundTilesVertice.addAll(getGroundTileVectors(1, -1));
        groundTilesVertice.stream().forEach(v -> v.setColor(WHITE));
        groundTilesVertice.stream().forEach(v -> v.setSelectColor(SelectionColor.NOTHING.getColorRGBA()));
        FloatBuffer verticesBuffer = buildVerticeBuffer(groundTilesVertice);
        IntBuffer indicesBuffer = buildIndicesBuffer(QUAD_INDICES, 10);

        List<DrawableObjectPart> tiles = new ArrayList<>();
        int nbIndicesPerQuad = QUAD_INDICES.length;
        for (int i = 0; i < 10; i++) {
            tiles.add(new DrawableObjectPart(i * nbIndicesPerQuad, nbIndicesPerQuad, false));
        }

        return buildDrawableObject(verticesBuffer, indicesBuffer, tiles, CubeAppTextures.GROUND_TEXTURE_NAME, null);
    }

    public static DrawableObject build3x3Faces() {
        List<Vertex> faceVertices = new ArrayList<>();
        faceVertices.addAll(getFaceVectors(-1, 1));
        faceVertices.addAll(getFaceVectors(0, 1));
        faceVertices.addAll(getFaceVectors(1, 1));
        faceVertices.addAll(getFaceVectors(-1, 0));
        faceVertices.addAll(getFaceVectors(0, 0));
        faceVertices.addAll(getFaceVectors(1, 0));
        faceVertices.addAll(getFaceVectors(-1, -1));
        faceVertices.addAll(getFaceVectors(0, -1));
        faceVertices.addAll(getFaceVectors(1, -1));
        faceVertices.stream().forEach(v -> v.setColor(RED));
        FloatBuffer verticesBuffer = buildVerticeBuffer(faceVertices);
        IntBuffer indicesBuffer = buildIndicesBuffer(QUAD_INDICES, 9);
        return buildDrawableObject(verticesBuffer, indicesBuffer, newSingleDrawableObjectPartAsList(QUAD_INDICES.length*9, false), CubeAppTextures.CUBE_TEXTURE_NAME, null);
    }
}
