package fr.kouignamann.cube.core;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.kouignamann.cube.core.utils.TextureUtils;

public class CubeAppTextures {

    private static Logger logger = LoggerFactory.getLogger(CubeAppTextures.class);

    public static final String CUBE_TEXTURE_NAME = "/textures/glass.png";
    public static final String GROUND_TEXTURE_NAME = "/textures/ground.jpg";

    private static final Map<String, String> TEXTURE_FILES_MAP = new HashMap<>();
    static {
        TEXTURE_FILES_MAP.put(CUBE_TEXTURE_NAME, "/textures/glass.png");
        TEXTURE_FILES_MAP.put(GROUND_TEXTURE_NAME, "/textures/ground.jpg");
    }

    private static CubeAppTextures textures;

    private Map<String, Integer> loadedTextures;

    public static void setup() {
        logger.info("Setting up CubeAppTextures");
        if (textures != null) {
            throw new IllegalStateException("CubeAppTextures already initialized");
        }
        textures = new CubeAppTextures();
        textures.loadedTextures = new HashMap<>();
    }

    public static void destroy() {
        logger.info("Destroying CubeAppTextures");
        checkCtx();
        textures.loadedTextures.values().forEach(TextureUtils::destroyTexture);
    }

    public static int getTextureId(String textureName) {
        checkCtx();
        if (textures.loadedTextures.containsKey(textureName)) {
            return textures.loadedTextures.get(textureName);
        }
        if (TEXTURE_FILES_MAP.containsKey(textureName)) {
            int textureId = TextureUtils.loadTexture(TEXTURE_FILES_MAP.get(textureName));
            textures.loadedTextures.put(textureName, textureId);
            return textureId;
        }
        throw new IllegalStateException(String.format("Texture with name '%s' is unknown from CubeApp", textureName));
    }

    public static void checkCtx() {
        if (textures == null) {
            throw new IllegalStateException("CubeAppTextures is null");
        }
    }

}
