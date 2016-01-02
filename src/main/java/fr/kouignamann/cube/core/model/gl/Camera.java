package fr.kouignamann.cube.core.model.gl;

import fr.kouignamann.cube.core.*;
import fr.kouignamann.cube.core.utils.*;
import org.lwjgl.util.vector.*;
import org.slf4j.*;

import static fr.kouignamann.cube.core.utils.MathUtils.RotationAxis.*;

public class Camera {

    private static final Logger logger = LoggerFactory.getLogger(Camera.class);

    private Matrix4f projectionMatrix = null;
    private Matrix4f viewMatrix = null;
    private Matrix4f modelMatrix = null;
    private Vector3f cameraPosition = null;
    private Vector3f cameraRotation = null;

    // Computed values
    private float[] xCameraRotationMatrix;
    private float[] yCameraRotationMatrix;
    private CameraMouvementVector3f cameraWalkVector = null;
    private CameraMouvementVector3f cameraStrafeVector = null;

    public Camera() {
        super();

        projectionMatrix = new Matrix4f();
        float fieldOfView = 60f;
        float aspectRatio = (float) Constant.SCREEN_WIDTH / (float)Constant.SCREEN_HEIGHT;
        float near_plane = 0.1f;
        float far_plane = 100f;

        float y_scale = MathUtils.coTangent((float)Math.toRadians(fieldOfView / 2f));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = far_plane - near_plane;

        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((far_plane + near_plane) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * near_plane * far_plane) / frustum_length);
        projectionMatrix.m33 = 0;

        viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();

        cameraPosition = Constant.INITIAL_CAMERA_POSITION;
        cameraRotation = Constant.INITIAL_CAMERA_ROTATION;
        cameraWalkVector = new CameraMouvementVector3f(true);
        cameraStrafeVector = new CameraMouvementVector3f(false);
        xCameraRotationMatrix = MathUtils.computeRotationMatrix(cameraRotation, X_AXIS, null);
        yCameraRotationMatrix = MathUtils.computeRotationMatrix(cameraRotation, Y_AXIS, null);
        computeMvtVectors();

        modelMatrix = new Matrix4f();
        Vector3f modelScale = new Vector3f(1.0f/Constant.SCALE, 1.0f/Constant.SCALE, 1.0f/Constant.SCALE);
        Matrix4f.scale(modelScale, modelMatrix, modelMatrix);
    }
	
    public Camera compute() {
        viewMatrix.setIdentity();
        
        Matrix4f.rotate(cameraRotation.x, X_AXIS.vector, viewMatrix, viewMatrix);
        Matrix4f.rotate(-cameraRotation.y, Y_AXIS.vector, viewMatrix, viewMatrix);
        Matrix4f.rotate(cameraRotation.z, Z_AXIS.vector, viewMatrix, viewMatrix);

        Matrix4f.translate(cameraPosition, viewMatrix, viewMatrix);

        return this;
    }

    private void computeMvtVectors() {
        MathUtils.computeRotationMatrix(cameraRotation, X_AXIS, xCameraRotationMatrix);
        MathUtils.computeRotationMatrix(cameraRotation, Y_AXIS, yCameraRotationMatrix);
        cameraWalkVector.rotate(xCameraRotationMatrix);
        cameraWalkVector.rotate(yCameraRotationMatrix);
        cameraStrafeVector.rotate(xCameraRotationMatrix);
        cameraStrafeVector.rotate(yCameraRotationMatrix);
        logger.info("New mvt vector ::");
        logger.info("walk = " + cameraWalkVector);
        logger.info("strafe = " + cameraStrafeVector);
        logger.info(" ---");
    }
	
    public void addRotation(float deltaX, float deltaY)
    {
    	cameraRotation.x -= deltaY;
    	cameraRotation.y -= deltaX;
        computeMvtVectors();
    }

    public void walk(boolean goForward) {
        if (goForward) {
            cameraPosition = Vector3f.add(cameraPosition, cameraWalkVector, cameraPosition);
        } else {
            cameraPosition = Vector3f.sub(cameraPosition, cameraWalkVector, cameraPosition);
        }
    }

    public void strafe(boolean strafeLeft) {
        if (strafeLeft) {
            cameraPosition = Vector3f.add(cameraPosition, cameraStrafeVector, cameraPosition);
        } else {
            cameraPosition = Vector3f.sub(cameraPosition, cameraStrafeVector, cameraPosition);
        }
    }
	
    public void addMovement(float movement)
    {
    	cameraPosition.z += movement;
    }

    public Matrix4f getProjectionMatrix() {
            return projectionMatrix;
    }

    public Matrix4f getViewMatrix() {
            return viewMatrix;
    }

	public Matrix4f getModelMatrix() {
		return modelMatrix;
	}
}
