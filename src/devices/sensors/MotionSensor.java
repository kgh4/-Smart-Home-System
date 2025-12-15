package devices.sensors;

import core.SmartDevice;
import events.Event;
import events.EventListener;
import enums.AlertType;
import structure.Home;

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

     // FIX (Person 5): MotionSensor does not know its Room.
     // Room context is handled by Home / AutomationEngine, so we only emit the motion event.
     Event motionEvent = new Event(
             AlertType.MOTION.name(),
             getDeviceName(),
             "Motion detected",
             null
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
