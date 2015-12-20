package fr.kouignamann.cube.core.listener;

import fr.kouignamann.cube.core.*;
import org.lwjgl.input.*;
import org.slf4j.*;

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
}
