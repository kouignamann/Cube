package fr.kouignamann.cube.core.listener;

import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.kouignamann.cube.core.Constant;
import fr.kouignamann.cube.core.CubeAppLogics;

public class KeyBoardListener implements Runnable {

	private Logger logger = LoggerFactory.getLogger(KeyBoardListener.class);

	private boolean keyhitOnCooldown = false;

	@Override
	public void run() {
		logger.info("Keyboard listening is active");
		while(true) {
			try {
				listen();
				if (keyhitOnCooldown == true) {
					Thread.sleep(Constant.KEY_HIT_MS_COOLDOWN);
					keyhitOnCooldown = false;
				} else {
					Thread.sleep(Constant.LISTENER_THREADS_MS_COOLDOWN);
				}
			} catch(InterruptedException | IllegalStateException e) {
				logger.info("Keyboard listening interuption");
				return;
			}
		}
	}

	private void listen() {
		if (Keyboard.isKeyDown(Keyboard.KEY_ADD)) {
			CubeAppLogics.registerCubeScale(true);
			keyhitOnCooldown = true;
			return;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)) {
			CubeAppLogics.registerCubeScale(false);
			keyhitOnCooldown = true;
			return;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6)) {
			CubeAppLogics.registerXCubeRotation(true);
			keyhitOnCooldown = true;
			return;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4)) {
			CubeAppLogics.registerXCubeRotation(false);
			keyhitOnCooldown = true;
			return;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8)) {
			CubeAppLogics.registerYCubeRotation(true);
			keyhitOnCooldown = true;
			return;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) {
			CubeAppLogics.registerYCubeRotation(false);
			keyhitOnCooldown = true;
			return;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD9)) {
			CubeAppLogics.registerZCubeRotation(true);
			keyhitOnCooldown = true;
			return;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1)) {
			CubeAppLogics.registerZCubeRotation(false);
			keyhitOnCooldown = true;
			return;
		}
	}
}
