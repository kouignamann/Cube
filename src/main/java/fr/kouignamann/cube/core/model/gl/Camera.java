package fr.kouignamann.cube.core.model.gl;

import fr.kouignamann.cube.core.*;
import fr.kouignamann.cube.core.utils.*;
import org.lwjgl.util.vector.*;

public class Camera {

    private Matrix4f projectionMatrix = null;
    private Matrix4f viewMatrix = null;
    private Matrix4f modelMatrix = null;
    private Vector3f cameraPosition = null;
    private Vector3f cameraRotation = null;
	
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

        modelMatrix = new Matrix4f();
        Vector3f modelScale = new Vector3f(1.0f/Constant.SCALE, 1.0f/Constant.SCALE, 1.0f/Constant.SCALE);
        Matrix4f.scale(modelScale, modelMatrix, modelMatrix);
    }
	
    public void compute() {
        viewMatrix.setIdentity();
        
        Matrix4f.translate(cameraPosition, viewMatrix, viewMatrix);
        
        Matrix4f.rotate(cameraRotation.x, MathUtils.X_AXIS_VECTOR, viewMatrix, viewMatrix);
        Matrix4f.rotate(cameraRotation.y, MathUtils.Y_AXIS_VECTOR, viewMatrix, viewMatrix);
        Matrix4f.rotate(cameraRotation.z, MathUtils.Z_AXIS_VECTOR, viewMatrix, viewMatrix);
    }
	
    public void addRotation(float deltaX, float deltaY)
    {
    	cameraRotation.x -= deltaY;
    	cameraRotation.y -= deltaX;
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
