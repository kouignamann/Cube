package fr.kouignamann.cube.core.listener;

public class CubeAppListeners {

    private static Thread keyboard = new Thread(new KeyBoardListener());
    private static Thread mouse = new Thread(new MouseListener());

    public static void startListeners() {
        keyboard.start();
        mouse.start();
    }

    public static void stopListeners() {
        keyboard.interrupt();
        mouse.interrupt();
    }
}
