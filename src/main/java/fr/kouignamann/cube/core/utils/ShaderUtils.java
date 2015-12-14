package fr.kouignamann.cube.core.utils;

import fr.kouignamann.cube.core.model.drawable.ShaderObject;
import org.lwjgl.opengl.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShaderUtils {

    private static Logger logger = LoggerFactory.getLogger(ShaderUtils.class);

    public static ShaderObject buildCubeAppShader() {
        logger.info("Building shader program");
        int shaderProgramId = GL20.glCreateProgram();
        int vertexShaderId = loadShader("/shaders/vertex.glsl", GL20.GL_VERTEX_SHADER);
        int fragmentShaderId = loadShader("/shaders/fragment.glsl", GL20.GL_FRAGMENT_SHADER);
        GL20.glAttachShader(shaderProgramId, vertexShaderId);
        GL20.glAttachShader(shaderProgramId, fragmentShaderId);
        GL20.glBindAttribLocation(shaderProgramId, 0, "in_Position");
        GL20.glBindAttribLocation(shaderProgramId, 1, "in_Color");
        GL20.glBindAttribLocation(shaderProgramId, 2, "in_TextureCoord");
        GL20.glLinkProgram(shaderProgramId);
        GL20.glValidateProgram(shaderProgramId);

        GlUtils.exitOnGLError("Shader initialization failure");
        return new ShaderObject(shaderProgramId, vertexShaderId, fragmentShaderId);
    }

    public static void destroyCubeAppShader(ShaderObject shaderObject) {
        logger.info("Destroying shader program");
        GL20.glUseProgram(0);
        GL20.glDetachShader(shaderObject.getShaderProgramId(), shaderObject.getVertexShaderId());
        GL20.glDetachShader(shaderObject.getShaderProgramId(), shaderObject.getFragmentShaderId());
        GL20.glDeleteShader(shaderObject.getVertexShaderId());
        GL20.glDeleteShader(shaderObject.getFragmentShaderId());
        GL20.glDeleteProgram(shaderObject.getShaderProgramId());
    }

    private static int loadShader(String filename, int type) {
        StringBuilder shaderSource = new StringBuilder();
        InputStream resourceInputStream = TextureUtils.class.getResourceAsStream(filename);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceInputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new IllegalStateException("Shader file i/o error", e);
        }

        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);

        return shaderID;
    }
}
