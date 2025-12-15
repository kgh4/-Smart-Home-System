package devices;

import core.SmartDevice;
import enums.AlertType;
import events.Event;

import java.util.ArrayList;
import java.util.List;

public class SmartLock extends SmartDevice {

    private boolean locked;
    private List<String> accessLog;

    public SmartLock(String deviceId, String deviceName) {
        super(deviceId, deviceName);
        this.locked = true; // default locked
        this.accessLog = new ArrayList<>();
    }

    public boolean isLocked() {
        return locked;
    }

    public void lock(String actor) {
        if (!locked) {
            locked = true;
            String entry = "[SmartLock] " + deviceName + " locked by " + actor;
            System.out.println(entry);
            accessLog.add(entry);
        }
    }

    public void unlock(String actor) {
        if (locked) {
            locked = false;
            String entry = "[SmartLock] " + deviceName + " unlocked by " + actor;
            System.out.println(entry);
            accessLog.add(entry);
        }
    }

    public List<String> getAccessLog() {
        return new ArrayList<>(accessLog);
    }

    @Override
    public void turnOn() {
        // For a lock, "power on" means electronics/actuator are powered
        super.turnOn();
    }

    @Override
    public double calculatePowerUsage() {
        // Simple model: small constant power when ON
        if (!isPoweredOn) {
            return 0.0;
        }
        return 5.0; // 5W when active
    }

    @Override
    public void executeScheduledTask() {
        // Example: ensure door is locked during scheduled check
        if (!locked) {
            System.out.println("[SmartLock] Scheduled task: auto-locking " + deviceName + ".");
            lock("AutoScheduler");
        } else {
            System.out.println("[SmartLock] Scheduled task: " + deviceName + " already locked.");
        }
    }

    @Override
    public void onEvent(Event event) {
        if (event == null) {
            return;
        }
        String type = event.getType();
        System.out.println("[SmartLock] " + deviceName + " received event: " + type + " from " + event.getSource());

        // Emergency alerts cause the lock to engage
        if (type.startsWith(AlertType.FIRE.name()) ||
            type.startsWith(AlertType.SMOKE.name()) ||
            type.startsWith(AlertType.EMERGENCY.name())) {

            lock("EmergencySystem");
            System.out.println("[SmartLock] Emergency alert. " + deviceName + " locked for safety.");
        }
    }

    @Override
    public void performAction() {
        // Main action: toggle lock state
        if (locked) {
            unlock("UserAction");
        } else {
            lock("UserAction");
        }
    }

    @Override
    public String toString() {
        return "SmartLock{" +
                "id='" + deviceId + '\'' +
                ", name='" + deviceName + '\'' +
                ", isOn=" + isPoweredOn +
                ", locked=" + locked +
                ", accessLogSize=" + accessLog.size() +
                '}';
    }
}
