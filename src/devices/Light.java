package devices;

import core.SmartDevice;
import enums.AlertType;
import events.Event;

public class Light extends SmartDevice {

    private int brightness;          // 0-100
    private String colorTemperature; // e.g. "WARM", "NEUTRAL", "COOL"
    private int autoOffMinutes;      // for scheduled task simulation

    public Light(String deviceId, String deviceName, int brightness, String colorTemperature) {
        super(deviceId, deviceName);
        this.brightness = Math.max(0, Math.min(100, brightness));
        this.colorTemperature = colorTemperature != null ? colorTemperature : "NEUTRAL";
        this.autoOffMinutes = 0;
    }

    // Brightness control
    public void setBrightness(int brightness) {
        if (brightness < 0 || brightness > 100) {
            System.out.println("[Light] Invalid brightness value (0-100 required). Keeping previous value.");
            return;
        }
        this.brightness = brightness;
        System.out.println("[Light] " + deviceName + " brightness set to " + brightness + "%.");
    }

    public int getBrightness() {
        return brightness;
    }

    // Color temperature control
    public void setColorTemperature(String colorTemperature) {
        if (colorTemperature == null || colorTemperature.isEmpty()) {
            System.out.println("[Light] Invalid color temperature value.");
            return;
        }
        this.colorTemperature = colorTemperature.toUpperCase();
        System.out.println("[Light] " + deviceName + " color temperature set to " + this.colorTemperature + ".");
    }

    public String getColorTemperature() {
        return colorTemperature;
    }

    // Schedule auto off in "minutes"
    public void scheduleAutoOff(int minutes) {
        if (minutes <= 0) {
            System.out.println("[Light] Auto-off minutes must be positive.");
            return;
        }
        this.autoOffMinutes = minutes;
        System.out.println("[Light] " + deviceName + " will auto-turn off in " + minutes + " minute(s) (simulated).");
    }

    @Override
    public double calculatePowerUsage() {
        // Simple model: base 10W at 100% brightness, proportional to brightness if ON, else 0
        if (!isPoweredOn) {
            return 0.0;
        }
        double maxWatts = 10.0;
        return maxWatts * (brightness / 100.0);
    }

    @Override
    public void executeScheduledTask() {
        // Simulate auto-off behavior
        if (isPoweredOn && autoOffMinutes > 0) {
            System.out.println("[Light] Auto-off task executed for " + deviceName + ". Turning OFF after " + autoOffMinutes + " minute(s) (simulated).");
            turnOff();
            autoOffMinutes = 0;
        } else {
            System.out.println("[Light] No scheduled task to execute for " + deviceName + ".");
        }
    }

    @Override
    public void onEvent(Event event) {
        if (event == null) {
            return;
        }

        String type = event.getType();
        System.out.println("[Light] " + deviceName + " received event: " + type + " from " + event.getSource());

        // Example: dim lights or turn them off for certain alerts
        if (type.startsWith(AlertType.FIRE.name()) || type.startsWith(AlertType.SMOKE.name())) {
            // Emergency: turn light ON at full brightness to illuminate escape path
            turnOn();
            setBrightness(100);
            System.out.println("[Light] Emergency detected. " + deviceName + " set to MAX brightness for safety.");
        } else if (type.startsWith(AlertType.TEMPERATURE.name())) {
            // Could react to temperature alerts (optional)
            System.out.println("[Light] Temperature alert received. No specific action configured.");
        }
    }

    @Override
    public void performAction() {
        // Main action could be toggling the light
        if (isPoweredOn) {
            System.out.println("[Light] Toggling OFF: " + deviceName);
            turnOff();
        } else {
            System.out.println("[Light] Toggling ON: " + deviceName);
            turnOn();
        }
    }

    @Override
    public String toString() {
        return "Light{" +
                "id='" + deviceId + '\'' +
                ", name='" + deviceName + '\'' +
                ", isOn=" + isPoweredOn +
                ", brightness=" + brightness +
                ", colorTemperature='" + colorTemperature + '\'' +
                '}';
    }
}
