package fr.kouignamann.cube.core.utils;

import org.lwjgl.opengl.*;
import org.slf4j.*;

import java.io.*;

public class ShaderUtils {

    private static Logger logger = LoggerFactory.getLogger(ShaderUtils.class);

    public static int loadShader(String filename, int type) {
        logger.info("Loading shader file : " + filename);
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
