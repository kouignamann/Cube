package fr.kouignamann.cube.core.listener;

import com.thalmic.myo.*;
import com.thalmic.myo.enums.*;
import fr.kouignamann.cube.core.*;
import fr.kouignamann.cube.core.utils.*;
import org.lwjgl.*;
import org.slf4j.*;

import java.nio.*;

public class MyoListener extends AbstractDeviceListener implements Runnable {

    private Logger logger = LoggerFactory.getLogger(MyoListener.class);

    private Hub cubeAppHub;

    private Myo cubeAppMyo;

    private ByteBuffer emgData;

    protected MyoListener() {
        super();
        GlUtils.initMyoLibs();
        this.cubeAppHub = new Hub("fr.kouignamann.cube");
        this.cubeAppHub.addListener(this);
        this.cubeAppMyo = this.cubeAppHub.waitForMyo(10000);

//        this.cubeAppMyo.unlock(UnlockType.UNLOCK_HOLD);
        if (this.cubeAppMyo != null) {
            logger.info("Myo armband found");
        }

    }

    @Override
    public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
//        logger.info(String.format("rotation %f, %f, %f, %f", rotation.getX(), rotation.getY(), rotation.getZ(), rotation.getW()));
    }

    @Override
    public void onPair(Myo myo, long timestamp, FirmwareVersion firmwareVersion) {
    }

    @Override
    public void onUnpair(Myo myo, long timestamp) {
    }

    @Override
    public void onConnect(Myo myo, long timestamp, FirmwareVersion firmwareVersion) {
    }

    @Override
    public void onDisconnect(Myo myo, long timestamp) {
    }

    @Override
    public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection, float rotation, WarmupState warmupState) {
    }

    @Override
    public void onArmUnsync(Myo myo, long timestamp) {
    }

    @Override
    public void onUnlock(Myo myo, long timestamp) {
    }

    @Override
    public void onLock(Myo myo, long timestamp) {
    }

    @Override
    public void onPose(Myo myo, long timestamp, Pose pose) {
        logger.info(String.format("pose %s", pose.getType().name()));
    }

    @Override
    public void onAccelerometerData(Myo myo, long timestamp, Vector3 accel) {
//        logger.info(String.format("accel %f, %f, %f", accel.getX(), accel.getY(), accel.getZ()));
    }

    @Override
    public void onGyroscopeData(Myo myo, long timestamp, Vector3 gyro) {
//        logger.info(String.format("gyro %f, %f, %f", gyro.getX(), gyro.getY(), gyro.getZ()));
    }

    @Override
    public void onRssi(Myo myo, long timestamp, int rssi) {
    }

    @Override
    public void onEmgData(Myo myo, long timestamp, byte[] emg) {
        if (this.emgData == null) {
            this.emgData = BufferUtils.createByteBuffer(emg.length);
        }
        this.emgData.put(emg);
        this.emgData.flip();
    }

    @Override
    public void onBatteryLevelReceived(Myo myo, long timestamp, int level) {
    }

    @Override
    public void onWarmupCompleted(Myo myo, long timestamp, WarmupResult warmupResult) {
    }

    @Override
    public void run() {
        if (this.cubeAppMyo == null) {
            logger.info("No myo armband found. Unable to listen");
            return;
        }
        logger.info("Myo listening is active");
        while (true) {
            try {
                this.cubeAppHub.runOnce(0);
//                Thread.sleep(0);
//                this.emgData.toString()
                Thread.sleep(Constant.LISTENER_THREADS_MS_COOLDOWN);
            }
            catch(InterruptedException e) {
                logger.info("Myo listening interuption");
                return;
            }
        }
    }
}
