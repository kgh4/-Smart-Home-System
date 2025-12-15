package test;

import structure.Home;
import devices.sensors.MotionSensor;
import devices.sensors.SmokeSensor;
import events.Event;
import events.EventListener;

// Dummy device to react to events
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

public class TestPerson2 {
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

        // Register sensors as listeners
        myHome.registerEventListener(hallwaySensor);
        myHome.registerEventListener(kitchenSensor);

        System.out.println("\n=== Testing MotionSensor ===");

        // FIX (Person 5): Removed getRoomType() from event message
        hallwaySensor.detectMotion(); // Broadcast MOTION event

        // Wait 6 seconds to auto-clear motion
        try { Thread.sleep(6000); } catch (InterruptedException e) {}
        hallwaySensor.executeScheduledTask(); // Clears motion after timeout
        System.out.println("Motion detected after timeout? " + hallwaySensor.isMotionDetected());

        System.out.println("\n=== Testing SmokeSensor ===");

        // FIX (Person 5): Removed getRoomType() from event message
        kitchenSensor.updateSmokeLevel(85); // Above threshold, should broadcast FIRE event
        System.out.println("Smoke detected? " + kitchenSensor.isSmokeDetected());
    }
}