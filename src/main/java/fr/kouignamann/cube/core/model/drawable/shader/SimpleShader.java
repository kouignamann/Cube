package fr.kouignamann.cube.core.model.drawable.shader;

import fr.kouignamann.cube.core.utils.*;
import org.lwjgl.opengl.*;

public class SimpleShader extends ShaderObject {

    public SimpleShader() {
        super();

        int shaderProgramId = GL20.glCreateProgram();
        int vertexShaderId = ShaderUtils.loadShader("/shaders/vertex.glsl", GL20.GL_VERTEX_SHADER);
        int fragmentShaderId = ShaderUtils.loadShader("/shaders/fragment.glsl", GL20.GL_FRAGMENT_SHADER);
        GL20.glAttachShader(shaderProgramId, vertexShaderId);
        GL20.glAttachShader(shaderProgramId, fragmentShaderId);
        GL20.glBindAttribLocation(shaderProgramId, 0, "in_Position");
        GL20.glBindAttribLocation(shaderProgramId, 1, "in_Color");
        GL20.glBindAttribLocation(shaderProgramId, 2, "in_TextureCoord");
        GL20.glLinkProgram(shaderProgramId);
        GL20.glValidateProgram(shaderProgramId);

        GlUtils.exitOnGLError("Shader initialization failure");
        setup(shaderProgramId, vertexShaderId, fragmentShaderId);
    }
}
