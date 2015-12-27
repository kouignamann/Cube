package fr.kouignamann.cube.core.builder;

import fr.kouignamann.cube.core.model.drawable.*;
import fr.kouignamann.cube.core.model.gl.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.slf4j.*;

import java.nio.*;
import java.util.*;

import static fr.kouignamann.cube.core.Constant.*;

public class CubeBuilder extends DrawableObjectBuilder {

    private final static Logger logger = LoggerFactory.getLogger(CubeBuilder.class);

    private final static float FACE_WIDTH = 100f;
    private final static float FACE_MARGIN = 1f;
    private final static float FACE_REAL_WIDTH = FACE_WIDTH - FACE_MARGIN;
    private final static float CUBE_UNIT = FACE_REAL_WIDTH / 2.0f;

    private final static int[] FACE_INDICES = {
            0,	1,	2,
            2,	3,	0
    };
    private final static int NB_VERTICE_PER_CUBE = 24;

    private static List<Vertex> getCubeVectors(int abs, int ord, int z) {
        return Arrays.asList(
                new Vertex(-CUBE_UNIT,  -CUBE_UNIT, CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 0f),    // FRONT FACE
                new Vertex(CUBE_UNIT,   -CUBE_UNIT, CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 1f),    // FRONT FACE
                new Vertex(CUBE_UNIT,   CUBE_UNIT,  CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 1f),    // FRONT FACE
                new Vertex(-CUBE_UNIT,  CUBE_UNIT,  CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 0f),    // FRONT FACE

                new Vertex(CUBE_UNIT,   -CUBE_UNIT, CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 0f),    // RIGHT FACE
                new Vertex(CUBE_UNIT,   -CUBE_UNIT, -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 1f),   // RIGHT FACE
                new Vertex(CUBE_UNIT,   CUBE_UNIT,  -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 1f),   // RIGHT FACE
                new Vertex(CUBE_UNIT,   CUBE_UNIT,  CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 0f),    // RIGHT FACE

                new Vertex(CUBE_UNIT,   -CUBE_UNIT, -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 0f),   // BACK FACE
                new Vertex(-CUBE_UNIT,  -CUBE_UNIT, -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 1f),   // BACK FACE
                new Vertex(-CUBE_UNIT,  CUBE_UNIT,  -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 1f),   // BACK FACE
                new Vertex(CUBE_UNIT,   CUBE_UNIT,  -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 0f),   // BACK FACE

                new Vertex(-CUBE_UNIT,  -CUBE_UNIT, -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 0f),   // LEFT FACE
                new Vertex(-CUBE_UNIT,  -CUBE_UNIT, CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 1f),    // LEFT FACE
                new Vertex(-CUBE_UNIT,  CUBE_UNIT,  CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 1f),    // LEFT FACE
                new Vertex(-CUBE_UNIT,  CUBE_UNIT,  -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 0f),   // LEFT FACE

                new Vertex(-CUBE_UNIT,  CUBE_UNIT,  CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 0f),    // TOP FACE
                new Vertex(CUBE_UNIT,   CUBE_UNIT,  CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 1f),    // TOP FACE
                new Vertex(CUBE_UNIT,   CUBE_UNIT,  -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 1f),   // TOP FACE
                new Vertex(-CUBE_UNIT,  CUBE_UNIT,  -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 0f),   // TOP FACE

                new Vertex(CUBE_UNIT,   -CUBE_UNIT, -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 0f),   // BOTTOM FACE
                new Vertex(-CUBE_UNIT,  -CUBE_UNIT, -CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(0f, 1f),   // BOTTOM FACE
                new Vertex(-CUBE_UNIT,  -CUBE_UNIT, CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 1f),    // BOTTOM FACE
                new Vertex(CUBE_UNIT,   -CUBE_UNIT, CUBE_UNIT).translate(abs*FACE_WIDTH, ord*FACE_WIDTH, z*FACE_WIDTH).setSt(1f, 0f)     // BOTTOM FACE
        );
    }

    public static void changeDrawableGeometry(DrawableObject drawableObject, float scale, float angle) {
        FloatBuffer verticeBuffer = drawableObject.getVerticeBuffer();
        FloatBuffer newVerticeBuffer = BufferUtils.createFloatBuffer(verticeBuffer.limit());

        List<Vertex> vertice = new ArrayList<>();
        while (verticeBuffer.hasRemaining()) {
            int i = NB_VERTICE_PER_CUBE;
            while (i > 0) {
                Vertex vertex = Vertex.readVertex(verticeBuffer);
                vertice.add(vertex);
                i--;
            }
            logger.info("Cube points = " + vertice.size());

            // TODO change geometry here
            vertice.stream().forEach(v -> v.setColor(GREEN));

            for (Vertex vertex : vertice) {
                newVerticeBuffer.put(vertex.getElements());
            }

            vertice.clear();
        }
        newVerticeBuffer.flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, drawableObject.getVboId());
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, newVerticeBuffer, GL15.GL_DYNAMIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        verticeBuffer.flip();
    }

    public static DrawableObject buildCube() {
        List<Vertex> faceVertices = getCubeVectors(0, 0, 0);
        faceVertices.stream().forEach(v -> v.setColor(RED));
        FloatBuffer verticesBuffer = buildVerticeBuffer(faceVertices);
        IntBuffer indicesBuffer = buildIndicesBuffer(FACE_INDICES, 6);
        return buildDrawableObject(verticesBuffer, indicesBuffer, null);
    }

    public static DrawableObject build3x3x3Cubes() {
        List<Vertex> cubeVertices = new ArrayList<>();
        List<DrawableObjectPart> cubeParts = new ArrayList<>();
        cubeVertices.addAll(getCubeVectors(-1, 1, 1));
        cubeVertices.addAll(getCubeVectors(0, 1, 1));
        cubeVertices.addAll(getCubeVectors(1, 1, 1));
        cubeVertices.addAll(getCubeVectors(-1, 0, 1));
        cubeVertices.addAll(getCubeVectors(0, 0, 1));
        cubeVertices.addAll(getCubeVectors(1, 0, 1));
        cubeVertices.addAll(getCubeVectors(-1, -1, 1));
        cubeVertices.addAll(getCubeVectors(0, -1, 1));
        cubeVertices.addAll(getCubeVectors(1, -1, 1));
        cubeVertices.addAll(getCubeVectors(-1, 1, 0));
        cubeVertices.addAll(getCubeVectors(0, 1, 0));
        cubeVertices.addAll(getCubeVectors(1, 1, 0));
        cubeVertices.addAll(getCubeVectors(-1, 0, 0));
        cubeVertices.addAll(getCubeVectors(0, 0, 0));
        cubeVertices.addAll(getCubeVectors(1, 0, 0));
        cubeVertices.addAll(getCubeVectors(-1, -1, 0));
        cubeVertices.addAll(getCubeVectors(0, -1, 0));
        cubeVertices.addAll(getCubeVectors(1, -1, 0));
        cubeVertices.addAll(getCubeVectors(-1, 1, -1));
        cubeVertices.addAll(getCubeVectors(0, 1, -1));
        cubeVertices.addAll(getCubeVectors(1, 1, -1));
        cubeVertices.addAll(getCubeVectors(-1, 0, -1));
        cubeVertices.addAll(getCubeVectors(0, 0, -1));
        cubeVertices.addAll(getCubeVectors(1, 0, -1));
        cubeVertices.addAll(getCubeVectors(-1, -1, -1));
        cubeVertices.addAll(getCubeVectors(0, -1, -1));
        cubeVertices.addAll(getCubeVectors(1, -1, -1));
        cubeVertices.stream().forEach(v -> v.setColor(RED));
        FloatBuffer verticesBuffer = buildVerticeBuffer(cubeVertices);
        IntBuffer indicesBuffer = buildIndicesBuffer(FACE_INDICES, 6 * 27);

        int nbIndicesPerCube = FACE_INDICES.length;
        for (int i = 0; i < cubeVertices.size(); i++) {
            cubeParts.add(new DrawableObjectPart(i*nbIndicesPerCube, nbIndicesPerCube));
        }
        return buildDrawableObject(verticesBuffer, indicesBuffer, cubeParts);
    }
}
