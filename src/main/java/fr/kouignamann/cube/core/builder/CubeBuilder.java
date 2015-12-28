package fr.kouignamann.cube.core.builder;

import fr.kouignamann.cube.core.model.drawable.*;
import fr.kouignamann.cube.core.model.gl.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;
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

    public static void changeDrawableGeometry(DrawableObject drawableObject, float scale, Vector3f angle) {
        FloatBuffer verticeBuffer = drawableObject.getVerticeBuffer();
        FloatBuffer newVerticeBuffer = BufferUtils.createFloatBuffer(verticeBuffer.limit());

        float realScale = 0.5f-(scale/200f);

        List<Vertex> vertice = new ArrayList<>();
        Map<Vector4fComparable, Vector4fComparable> positionsMap = new HashMap<>();
        while (verticeBuffer.hasRemaining()) {
            // One pass per cube
            int i = NB_VERTICE_PER_CUBE;
            while (i > 0) {
                Vertex vertex = Vertex.readVertex(verticeBuffer);
                Vector4fComparable position = new Vector4fComparable(vertex);
                vertice.add(vertex);
                if (!positionsMap.containsKey(position)) {
                    positionsMap.put(position, position.clone());
                }
                i--;
            }

            List<Vector4fComparable> orderedPositionsToChange =
                    Vector4fComparable.orderOpposedVertex(new ArrayList<>(positionsMap.values()));

//            Vector4f rotationCenter = (Vector4f) Vector4f.add(
//                    orderedPositionsToChange.get(0),
//                    orderedPositionsToChange.get(1),
//                    null)
//                    .scale(0.5f);

            for (int index=0; index< orderedPositionsToChange.size(); ) {
                Vector4f vector1 = orderedPositionsToChange.get(index++);
                Vector4f vector2 = orderedPositionsToChange.get(index++);

                // Translation
                Vector4f translationVector = (Vector4f) Vector4f.sub(vector1, vector2, null).scale(realScale);
                Vector4f.sub(vector1, translationVector, vector1);
                Vector4f.add(vector2, translationVector, vector2);

                // Rotation
            }

            for (Vertex vertex : vertice) {
                Vector4fComparable vectorComparable = new Vector4fComparable(vertex);
                vertex.setPosition(positionsMap.get(vectorComparable));
                newVerticeBuffer.put(vertex.getElements());
            }

            vertice.clear();
            positionsMap.clear();
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
