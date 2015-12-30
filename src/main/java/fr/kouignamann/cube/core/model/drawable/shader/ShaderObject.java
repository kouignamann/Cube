package fr.kouignamann.cube.core.model.drawable.shader;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL20;

import fr.kouignamann.cube.core.model.gl.Camera;

public abstract class ShaderObject {

    private int shaderProgramId;
    private int vertexShaderId;
    private int fragmentShaderId;

    protected int projectionMatrixLocation;
    protected int viewMatrixLocation;
    protected int modelMatrixLocation;

    protected FloatBuffer matrix44Buffer = null;

    protected ShaderObject() {
        super();
    }

    protected void setup(int shaderProgramId, int vertexShaderId, int fragmentShaderId) {
        this.shaderProgramId = shaderProgramId;
        this.vertexShaderId = vertexShaderId;
        this.fragmentShaderId = fragmentShaderId;
    }

    public void pushUniforms(Camera camera) {
        camera.getProjectionMatrix().store(matrix44Buffer);
        matrix44Buffer.flip();
        GL20.glUniformMatrix4(projectionMatrixLocation, false, matrix44Buffer);

        camera.getViewMatrix().store(matrix44Buffer);
        matrix44Buffer.flip();
        GL20.glUniformMatrix4(viewMatrixLocation, false, matrix44Buffer);

        camera.getModelMatrix().store(matrix44Buffer);
        matrix44Buffer.flip();
        GL20.glUniformMatrix4(modelMatrixLocation, false, matrix44Buffer);
    }

    public void destroy() {
        GL20.glUseProgram(0);
        GL20.glDetachShader(shaderProgramId, vertexShaderId);
        GL20.glDetachShader(shaderProgramId, fragmentShaderId);
        GL20.glDeleteShader(vertexShaderId);
        GL20.glDeleteShader(fragmentShaderId);
        GL20.glDeleteProgram(shaderProgramId);
    }

    public int getShaderProgramId() {
        return shaderProgramId;
    }
}
