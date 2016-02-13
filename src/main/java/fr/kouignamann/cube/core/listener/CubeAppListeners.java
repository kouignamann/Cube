package fr.kouignamann.cube.core.listener;

public class CubeAppListeners {

    private static Thread keyboard = new Thread(new KeyBoardListener());
    private static Thread mouse = new Thread(new MouseListener());
//    private static Thread myo = new Thread(new MyoListener());

    public static void startListeners() {
        keyboard.start();
        mouse.start();
//        myo.start();
    }

    public static void stopListeners() {
        keyboard.interrupt();
        mouse.interrupt();
//        myo.interrupt();
    }
}
