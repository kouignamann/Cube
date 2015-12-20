package fr.kouignamann.cube.core.model.drawable.shader;

import fr.kouignamann.cube.core.model.gl.*;
import fr.kouignamann.cube.core.utils.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;

import java.nio.*;

public class ShaderObject {

    private int shaderProgramId;
    private int vertexShaderId;
    private int fragmentShaderId;

    private int projectionMatrixLocation;
    private int viewMatrixLocation;
    private int modelMatrixLocation;

    private FloatBuffer matrix44Buffer = null;

    public ShaderObject() {
        super();

        shaderProgramId = GL20.glCreateProgram();
        vertexShaderId = ShaderUtils.loadShader("/shaders/vertex.glsl", GL20.GL_VERTEX_SHADER);
        fragmentShaderId = ShaderUtils.loadShader("/shaders/fragment.glsl", GL20.GL_FRAGMENT_SHADER);
        GL20.glAttachShader(shaderProgramId, vertexShaderId);
        GL20.glAttachShader(shaderProgramId, fragmentShaderId);
        GL20.glBindAttribLocation(shaderProgramId, 0, "in_Position");
        GL20.glBindAttribLocation(shaderProgramId, 1, "in_Color");
        GL20.glBindAttribLocation(shaderProgramId, 2, "in_TextureCoord");
        GL20.glLinkProgram(shaderProgramId);
        GL20.glValidateProgram(shaderProgramId);

        projectionMatrixLocation = GL20.glGetUniformLocation(shaderProgramId, "projectionMatrix");
        viewMatrixLocation = GL20.glGetUniformLocation(shaderProgramId, "viewMatrix");
        modelMatrixLocation = GL20.glGetUniformLocation(shaderProgramId, "modelMatrix");

        matrix44Buffer = BufferUtils.createFloatBuffer(16);

        setup(shaderProgramId, vertexShaderId, fragmentShaderId);
    }

    public void setup(int shaderProgramId, int vertexShaderId, int fragmentShaderId) {
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
