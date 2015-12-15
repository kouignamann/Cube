package fr.kouignamann.cube.core;

import fr.kouignamann.cube.core.builder.FaceBuilder;
import fr.kouignamann.cube.core.model.drawable.DrawableObject;

import java.util.ArrayList;
import java.util.List;

public class CubeAppLogics {

    private static CubeAppLogics logics;

    private List<DrawableObject> dObjects;

    private CubeAppLogics() {
        dObjects = new ArrayList<>();
    }

    public static void initLogics() {
        if (logics != null) {
            logics.dObjects.stream().forEach(DrawableObject::destroy);
        }
        logics = new CubeAppLogics();

        logics.dObjects.add(FaceBuilder.build3x3Faces());
    }

    public static void destroyLogics() {
        if (logics == null) {
            throw new IllegalStateException("CubeAppLogics is null");
        }
        logics.dObjects.stream().forEach(DrawableObject::destroy);
        logics = null;
    }

    public static List<DrawableObject> getDrawables() {
        if (logics==null) {
            throw new IllegalStateException("CubeAppLogics wasn t initialized properly (use 'initLogics')");
        }
        return logics.dObjects;
    }
}
