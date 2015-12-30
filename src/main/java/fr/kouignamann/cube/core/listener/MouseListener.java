package fr.kouignamann.cube.core.listener;

import fr.kouignamann.cube.core.*;
import org.lwjgl.input.*;
import org.slf4j.*;

import java.util.*;

public class MouseListener implements Runnable {

    private Logger logger = LoggerFactory.getLogger(MouseListener.class);
	
    private float xMovement;
    private float yMovement;
	
    private boolean leftClick;

    private long lastClickMillis = 0;

    private Integer xPressed;
    private Integer yPressed;

    private Integer xReleased;
    private Integer yReleased;
    
    public MouseListener() {
        super();
        Mouse.setClipMouseCoordinatesToWindow(false);
        xMovement = 0;
        yMovement = 0;
    }

    @Override
    public void run() {
        logger.info("Mouse listening is active");
        while(true) {
            try {
                listen();
                Thread.sleep(Constant.LISTENER_THREADS_MS_COOLDOWN);
            } catch(InterruptedException | IllegalStateException e) {
                logger.info("Mouse listening interuption");
                return;
            }
        }
    }

    public void listen() {
        listenLeftClick();
        listenWheel();
    }

    private void listenWheel() {
        float dWheel;
        if ((dWheel = (float)Mouse.getDWheel()) != 0) {
            CubeAppGraphics.addCameraMovement((dWheel * Constant.WHEEL_SENSITIVITY) / 10000.0f);
        }
    }
    
    private void listenLeftClick() {
        if (Mouse.isButtonDown(0)) {
            if (leftClick) {
                if (!this.isInsideWindow()) {
                    leftClick = false;
                } else {
                    xReleased = Mouse.getX();
                    yReleased = Mouse.getY();
                    leftAction();
                }
            }
            else {
                leftClick = true;
                xPressed = Mouse.getX();
                yPressed = Mouse.getY();
            }
        } else {
            if (leftClick) {
                if (this.isInsideWindow()) {
                    xReleased = Mouse.getX();
                    yReleased = Mouse.getY();
                    leftAction();
                } else {
                    leftClick = false;
                }
            }
            leftClick = false;
        }
    }

    private void leftAction() {
        if (Objects.equals(xPressed,xReleased)
                && Objects.equals(yPressed, yReleased)) {
            this.leftSelectionClick();
        } else {
            this.leftRotationClick();
        }
    }

    private boolean isInsideWindow() {
        return (Mouse.getX()>=0
                && Mouse.getX() <= Constant.SCREEN_WIDTH
                && Mouse.getY()>=0
                && Mouse.getY() <= Constant.SCREEN_HEIGHT);
    }

    private void leftSelectionClick() {
        if (Constant.CLICK_MS_COOLDOWN + lastClickMillis < System.currentTimeMillis()) {
            lastClickMillis = System.currentTimeMillis();
            CubeAppLogics.registerScreenClick(Mouse.getX(), Mouse.getY());
        }
    }

    private void leftRotationClick() {
        xMovement = (xReleased - xPressed)*Constant.MOUSE_SENSITIVITY/500000.f;
        yMovement = (yReleased - yPressed)*Constant.MOUSE_SENSITIVITY/500000.f;

        CubeAppGraphics.addCameraRotation(xMovement, yMovement);
    }
}
