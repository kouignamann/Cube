package fr.kouignamann.cube.core.utils;

import fr.kouignamann.cube.core.model.drawable.shader.*;
import org.lwjgl.opengl.*;
import org.slf4j.*;

import java.io.*;

public class ShaderUtils {

    private static Logger logger = LoggerFactory.getLogger(ShaderUtils.class);

    public static void destroyCubeAppShader(ShaderObject shaderObject) {
        logger.info("Destroying shader program");
        GL20.glUseProgram(0);
        GL20.glDetachShader(shaderObject.getShaderProgramId(), shaderObject.getVertexShaderId());
        GL20.glDetachShader(shaderObject.getShaderProgramId(), shaderObject.getFragmentShaderId());
        GL20.glDeleteShader(shaderObject.getVertexShaderId());
        GL20.glDeleteShader(shaderObject.getFragmentShaderId());
        GL20.glDeleteProgram(shaderObject.getShaderProgramId());
    }

    public static int loadShader(String filename, int type) {
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
