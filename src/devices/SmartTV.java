package devices;

import core.SmartDevice;
import enums.AlertType;
import events.Event;

public class SmartTV extends SmartDevice {

    private int currentChannel;
    private int volume;          // 0-100
    private boolean muted;
    private boolean emergencyShutdown;

    public SmartTV(String deviceId, String deviceName, int initialChannel, int initialVolume) {
        super(deviceId, deviceName);
        this.currentChannel = Math.max(1, initialChannel);
        this.volume = Math.max(0, Math.min(100, initialVolume));
        this.muted = false;
        this.emergencyShutdown = false;
    }

    public void setChannel(int channel) {
        if (channel <= 0) {
            System.out.println("[SmartTV] Invalid channel number.");
            return;
        }
        this.currentChannel = channel;
        System.out.println("[SmartTV] " + deviceName + " switched to channel " + currentChannel + ".");
    }

    public int getCurrentChannel() {
        return currentChannel;
    }

    public void setVolume(int volume) {
        if (volume < 0 || volume > 100) {
            System.out.println("[SmartTV] Invalid volume (0-100).");
            return;
        }
        this.volume = volume;
        System.out.println("[SmartTV] " + deviceName + " volume set to " + volume + ".");
    }

    public int getVolume() {
        return volume;
    }

    public void mute() {
        this.muted = true;
        System.out.println("[SmartTV] " + deviceName + " muted.");
    }

    public void unmute() {
        this.muted = false;
        System.out.println("[SmartTV] " + deviceName + " unmuted.");
    }

    public boolean isMuted() {
        return muted;
    }

    @Override
    public void turnOn() {
        if (emergencyShutdown) {
            System.out.println("[SmartTV] " + deviceName + " cannot be turned on due to emergency shutdown state.");
            return;
        }
        super.turnOn();
    }

    @Override
    public double calculatePowerUsage() {
        if (!isPoweredOn) {
            return 0.0;
        }
        // Simple model: 80W base when ON, a bit more with high volume
        double baseWatts = 80.0;
        double volumeFactor = 1.0 + (volume / 200.0); // up to +50%
        return baseWatts * volumeFactor;
    }

    @Override
    public void executeScheduledTask() {
        // Example: scheduled task could be auto power-off at night
        if (isPoweredOn) {
            System.out.println("[SmartTV] Scheduled task: turning OFF " + deviceName + " (e.g., 22:00 rule).");
            turnOff();
        } else {
            System.out.println("[SmartTV] Scheduled task: " + deviceName + " already OFF.");
        }
    }

    @Override
    public void onEvent(Event event) {
        if (event == null) {
            return;
        }
        String type = event.getType();
        System.out.println("[SmartTV] " + deviceName + " received event: " + type + " from " + event.getSource());

        // Emergency rules: FIRE/SMOKE alerts turn TVs off
        if (type.startsWith(AlertType.FIRE.name()) ||
            type.startsWith(AlertType.SMOKE.name()) ||
            type.startsWith(AlertType.EMERGENCY.name())) {

            if (isPoweredOn) {
                System.out.println("[SmartTV] Emergency alert. Turning OFF " + deviceName + " for safety.");
                turnOff();
            }
            emergencyShutdown = true;
        }
        // Non-critical alerts can show messages without power change
        else if (type.startsWith(AlertType.MOTION.name())) {
            System.out.println("[SmartTV] Motion alert received (no direct action configured).");
        }
    }

    @Override
    public void performAction() {
        // Example main action: switch to a default news channel
        if (!isPoweredOn) {
            turnOn();
        }
        setChannel(1);
        System.out.println("[SmartTV] " + deviceName + " performing action: switching to channel 1.");
    }

    @Override
    public String toString() {
        return "SmartTV{" +
                "id='" + deviceId + '\'' +
                ", name='" + deviceName + '\'' +
                ", isOn=" + isPoweredOn +
                ", channel=" + currentChannel +
                ", volume=" + volume +
                ", muted=" + muted +
                ", emergencyShutdown=" + emergencyShutdown +
                '}';
    }
}
