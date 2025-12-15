package devices.sensors;

import core.SmartDevice;
import events.Event;
import events.EventListener;
import enums.AlertType;
import structure.Home;

public class SmokeSensor extends SmartDevice implements EventListener {

    private double smokeLevel;
    private double threshold;
    private boolean smokeDetected;
    private Home home; // reference to Home

    public SmokeSensor(String id, String name, double threshold, Home home) {
        super(id, name);
        this.threshold = threshold;
        this.smokeDetected = false;
        this.home = home;
    }

    public void updateSmokeLevel(double value) {
        this.smokeLevel = value;

        if (smokeLevel >= threshold && !smokeDetected) {
            smokeDetected = true;

         // FIX (Person 5): SmokeSensor does not know its Room.
         // Room context is handled by Home / AutomationEngine.
         Event alertEvent = new Event(
                 AlertType.FIRE.name(),
                 getDeviceName(),
                 "Smoke level critical",
                 null
         );

            try {
                home.emitEvent(alertEvent); // broadcast via Home
            } catch (Exception e) {
                System.out.println("[SmokeSensor] Error broadcasting alert: " + e.getMessage());
            }
        }
    }

    public boolean isSmokeDetected() {
        return smokeDetected;
    }

    @Override
    public void onEvent(Event event) {
        // Smoke sensors do not react to events
    }

    @Override
    public double calculatePowerUsage() {
        // Example: smoke sensors consume 2 W when ON
        return isPoweredOn ? 2.0 : 0.0;
    }

    @Override
    public void executeScheduledTask() {
        // For simplicity, no scheduled task
    }

    @Override
    public void performAction() {
        // Could simulate a smoke test
    }
}
