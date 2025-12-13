package main;

import devices.structure.Home;
import devices.sensors.MotionSensor;
import devices.sensors.SmokeSensor;
import enums.RoomType;
import events.Event;
import events.EventListener;

// Example dummy device to react to events
class DummyDevice implements EventListener {
    private String name;

    public DummyDevice(String name) {
        this.name = name;
    }

    @Override
    public void onEvent(Event event) {
        System.out.println("[DummyDevice: " + name + "] Received event -> Type: "
                + event.getType() + ", Source: " + event.getSource() + ", Message: " + event.getMessage());
    }
}

public class Main {
    public static void main(String[] args) {
        // Create Home
        Home myHome = new Home("H001", "SmartHome");

        // Register dummy devices to listen to events
        DummyDevice tv = new DummyDevice("TV");
        DummyDevice lock = new DummyDevice("SmartLock");
        myHome.registerEventListener(tv);
        myHome.registerEventListener(lock);

        // Create Sensors
        MotionSensor hallwaySensor = new MotionSensor("MS1", "Hallway Motion Sensor", 5, myHome);
        SmokeSensor kitchenSensor = new SmokeSensor("SS1", "Kitchen Smoke Sensor", 70, myHome);

        // Register sensors as listeners (optional, if you want sensors to receive events)
        myHome.registerEventListener(hallwaySensor);
        myHome.registerEventListener(kitchenSensor);

        System.out.println("\n=== Testing MotionSensor ===");
        hallwaySensor.detectMotion(); // Should broadcast MOTION event

        // Wait 6 seconds to auto-clear motion
        try { Thread.sleep(6000); } catch (InterruptedException e) {}
        hallwaySensor.executeScheduledTask(); // Clears motion after timeout
        System.out.println("Motion detected after timeout? " + hallwaySensor.isMotionDetected());

        System.out.println("\n=== Testing SmokeSensor ===");
        kitchenSensor.updateSmokeLevel(85); // Above threshold, should broadcast FIRE event
        System.out.println("Smoke detected? " + kitchenSensor.isSmokeDetected());
    }
}
