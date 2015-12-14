package fr.kouignamann.cube.core.controller;

import fr.kouignamann.cube.core.Constant;
import fr.kouignamann.cube.core.CubeAppGraphics;
import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyBoardListener implements Runnable {

	private Logger logger = LoggerFactory.getLogger(KeyBoardListener.class);

	private boolean keyhitOnCooldown = false;

	public void listen() {
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			CubeAppGraphics.previousTexture();
			keyhitOnCooldown = true;
			return;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			CubeAppGraphics.nextTexture();
			keyhitOnCooldown = true;
			return;
		}
	}

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
					Thread.sleep(Constant.LISTENER_THREADS_COOLDOWN);
				}
			} catch(InterruptedException | IllegalStateException e) {
				logger.info("Keyboard listening interuption");
				return;
			}
		}
	}
}
