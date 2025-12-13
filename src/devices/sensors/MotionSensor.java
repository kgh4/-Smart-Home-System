package devices.sensors;

import core.SmartDevice;
import events.Event;
import events.EventListener;
import enums.AlertType;
import devices.structure.Home;

public class MotionSensor extends SmartDevice implements EventListener {

    private int timeoutSeconds;
    private long lastMotionTimestamp;
    private boolean motionDetected;
    private Home home; // reference to Home

    public MotionSensor(String id, String name, int timeoutSeconds, Home home) {
        super(id, name);
        this.timeoutSeconds = timeoutSeconds;
        this.motionDetected = false;
        this.home = home;
    }

    // Triggered when motion is detected
    public void detectMotion() {
        motionDetected = true;
        lastMotionTimestamp = System.currentTimeMillis();

        Event motionEvent = new Event(
                AlertType.MOTION.name(),
                getDeviceName(),
                "Motion detected in " + getRoomType()
        );

        home.emitEvent(motionEvent); // broadcast to all listeners
    }

    // Auto-clear motion after timeout
    public void clearMotionIfTimedOut() {
        long now = System.currentTimeMillis();
        if (motionDetected && (now - lastMotionTimestamp) / 1000 >= timeoutSeconds) {
            motionDetected = false;
        }
    }

    public boolean isMotionDetected() {
        return motionDetected;
    }

    @Override
    public void onEvent(Event event) {
        // Sensors do not react to events
    }

    @Override
    public double calculatePowerUsage() {
        // Example: motion sensors consume 1 W when ON
        return isPoweredOn ? 1.0 : 0.0;
    }

    @Override
    public void executeScheduledTask() {
        clearMotionIfTimedOut();
    }

    @Override
    public void performAction() {
        detectMotion();
    }
}
