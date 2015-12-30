package fr.kouignamann.cube.core.model.drawable.shader;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import fr.kouignamann.cube.core.utils.ShaderUtils;

public class SimpleCubeShader extends ShaderObject {

    public SimpleCubeShader() {
        super();

        int shaderProgramId = GL20.glCreateProgram();
        int vertexShaderId = ShaderUtils.loadShader("/shaders/simpleCubeVertex.glsl", GL20.GL_VERTEX_SHADER);
        int fragmentShaderId = ShaderUtils.loadShader("/shaders/simpleCubeFragment.glsl", GL20.GL_FRAGMENT_SHADER);
        GL20.glAttachShader(shaderProgramId, vertexShaderId);
        GL20.glAttachShader(shaderProgramId, fragmentShaderId);
        GL20.glBindAttribLocation(shaderProgramId, 0, "in_Position");
        GL20.glBindAttribLocation(shaderProgramId, 1, "in_Color");
        GL20.glBindAttribLocation(shaderProgramId, 2, "in_SelectColor");
        GL20.glBindAttribLocation(shaderProgramId, 3, "in_TextureCoord");
        GL20.glLinkProgram(shaderProgramId);
        GL20.glValidateProgram(shaderProgramId);

        projectionMatrixLocation = GL20.glGetUniformLocation(shaderProgramId, "projectionMatrix");
        viewMatrixLocation = GL20.glGetUniformLocation(shaderProgramId, "viewMatrix");
        modelMatrixLocation = GL20.glGetUniformLocation(shaderProgramId, "modelMatrix");

        matrix44Buffer = BufferUtils.createFloatBuffer(16);

        setup(shaderProgramId, vertexShaderId, fragmentShaderId);
    }
}
