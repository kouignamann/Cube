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
    private final static float FACE_MARGIN = 1;
    private final static float FACE_REAL_WIDTH = FACE_WIDTH - FACE_MARGIN;
    private final static float CUBE_UNIT = FACE_REAL_WIDTH / 2.0f;

    private final static int[] CUBE_INDICES = {
            0,	1,	2,  // FRONT FACE
            2,	3,	0,  // FRONT FACE
            4,	5,	6,  // RIGHT FACE
            6,	7,	4,  // RIGHT FACE
            8,	9,	10, // BACK FACE
            10,	11,	8,  // BACK FACE
            12,	13,	14, // LEFT FACE
            14,	15,	12, // LEFT FACE
            16,	17,	18, // TOP FACE
            18,	19,	16, // TOP FACE
            20,	21,	22, // BOTTOM FACE
            22,	23,	20  // BOTTOM FACE
    };
    private final static int NB_VERTEX_PER_CUBE = 24;

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

    public static void changeDrawableGeometry(DrawableObject drawableObject) {
        FloatBuffer verticeBuffer = drawableObject.getVerticeBuffer();
        FloatBuffer newVerticeBuffer = BufferUtils.createFloatBuffer(verticeBuffer.limit());

        List<Vertex> vertice = new ArrayList<>();
        Map<CubAppVector4f, CubAppVector4f> positionsMap = new HashMap<>();
        Iterator<DrawableObjectPart> drawableObjectPartIterator = drawableObject.getParts().iterator();
        DrawableObjectPart part = null;
        while (verticeBuffer.hasRemaining() && drawableObjectPartIterator.hasNext()) {
            // One pass per cube
            part = drawableObjectPartIterator.next();
            int i = NB_VERTEX_PER_CUBE;
            while (i > 0) {
                Vertex vertex = Vertex.readVertex(verticeBuffer);
                CubAppVector4f position = new CubAppVector4f(vertex);
                vertice.add(vertex);
                if (!positionsMap.containsKey(position)) {
                    positionsMap.put(position, position.clone());
                }
                i--;
            }

            List<CubAppVector4f> orderedPositionsToChange =
                    CubAppVector4f.orderOpposedVertex(new ArrayList<>(positionsMap.values()));

            Vector4f rotationCenter = (Vector4f) Vector4f.add(
                    orderedPositionsToChange.get(0),
                    orderedPositionsToChange.get(1),
                    null)
                    .scale(0.5f);

            for (int index=0; index< orderedPositionsToChange.size(); ) {
                CubAppVector4f vector1 = orderedPositionsToChange.get(index++);
                CubAppVector4f vector2 = orderedPositionsToChange.get(index++);

                // Translation
                Vector4f translationVector = (Vector4f) Vector4f.sub(vector1, vector2, null).scale(part.getRealScale());
                Vector4f.sub(vector1, translationVector, vector1);
                Vector4f.add(vector2, translationVector, vector2);

                // Rotations
                vector1.rotate(rotationCenter, part.getxRotationMatrix(), part.getyRotationMatrix(), part.getzRotationMatrix());
                vector2.rotate(rotationCenter, part.getxRotationMatrix(), part.getyRotationMatrix(), part.getzRotationMatrix());
            }

            for (Vertex vertex : vertice) {
                CubAppVector4f vectorComparable = new CubAppVector4f(vertex);
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
        logger.info("Building single cube");
        List<Vertex> faceVertices = getCubeVectors(0, 0, 0);
        faceVertices.stream().forEach(v -> v.setColor(RED));
        FloatBuffer verticesBuffer = buildVerticeBuffer(faceVertices);
        IntBuffer indicesBuffer = buildIndicesBuffer(CUBE_INDICES, 1);
        return buildDrawableObject(verticesBuffer, indicesBuffer, null);
    }

    public static DrawableObject build3x3x3Cubes() {
        logger.info("Building 3 x 3 x 3 cubes");
        List<Vertex> cubeVertices = new ArrayList<>();
        List<DrawableObjectPart> cubes = new ArrayList<>();
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

        int nbIndicesPerCube = CUBE_INDICES.length;
        for (int i = 0; i < 27; i++) {
            cubes.add(new DrawableObjectPart(i * nbIndicesPerCube, nbIndicesPerCube));
        }
        for (DrawableObjectPart part : cubes) {
            for (int i = part.getStartVertexIndex(); i<=part.getLastVertexIndex(); i++) {
                cubeVertices.get(i).setSelectColor(part.getSelectionColor().getColorRGBA());
            }
        }

        FloatBuffer verticesBuffer = buildVerticeBuffer(cubeVertices);
        IntBuffer indicesBuffer = buildIndicesBuffer(CUBE_INDICES, 27);

        return buildDrawableObject(verticesBuffer, indicesBuffer, cubes);
    }
}
