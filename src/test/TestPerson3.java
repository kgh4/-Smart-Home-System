package test;

import devices.Light;
import devices.SmartLock;
import devices.SmartTV;
import enums.AlertType;
import enums.RoomType;
import exceptions.EmergencyAlertException;
import structure.Home;
import structure.Room;

public class TestPerson3 {

    public static void main(String[] args) {

        // 1. Create home and rooms
        Home myHome = new Home("HOME001", "Person3 Demo Home");
        Room livingRoom = new Room("ROOM001", "Living Room", RoomType.LIVING_ROOM);
        Room bedroom = new Room("ROOM002", "Bedroom", RoomType.BEDROOM);
        Room kitchen = new Room("ROOM003", "Kitchen", RoomType.KITCHEN);

        myHome.addRoom(livingRoom);
        myHome.addRoom(bedroom);
        myHome.addRoom(kitchen);

        // 2. Create Person 3 devices
        Light livingRoomLight = new Light("LIGHT001", "Living Room Main Light", 60, "WARM");
        SmartTV livingRoomTV = new SmartTV("TV001", "Living Room TV", 5, 30);
        SmartLock bedroomLock = new SmartLock("LOCK001", "Bedroom Door Lock");

        // 3. Add devices to rooms
        livingRoom.addDevice(livingRoomLight);
        livingRoom.addDevice(livingRoomTV);
        bedroom.addDevice(bedroomLock);

        // Important: register devices as event listeners in Home
        myHome.registerEventListener(livingRoomLight);
        myHome.registerEventListener(livingRoomTV);
        myHome.registerEventListener(bedroomLock);

        // 4. Basic control and status
        System.out.println("\n=== Turning ON all devices ===");
        myHome.turnOnAllDevices();
        livingRoomLight.setBrightness(80);
        livingRoomLight.setColorTemperature("NEUTRAL");
        livingRoomLight.scheduleAutoOff(5);

        livingRoomTV.setChannel(10);
        livingRoomTV.setVolume(40);

        System.out.println("\n=== Initial Home Status ===");
        myHome.printHomeStatus();

        // 5. Execute scheduled tasks (auto-off, auto-lock, etc.)
        System.out.println("\n=== Executing Scheduled Tasks ===");
        livingRoomLight.executeScheduledTask();
        livingRoomTV.executeScheduledTask();
        bedroomLock.executeScheduledTask();

        System.out.println("\n=== Status After Scheduled Tasks ===");
        myHome.printHomeStatus();

        // 6. Simulate a SMOKE alert from the kitchen
        System.out.println("\n=== Simulating SMOKE alert from Kitchen ===");
        try {
            myHome.broadcastAlert(AlertType.SMOKE, "Kitchen", "Smoke detected in kitchen!");
        } catch (EmergencyAlertException e) {
            System.out.println("EmergencyAlertException: " + e.getMessage());
        }

        System.out.println("\n=== Status After SMOKE Alert ===");
        myHome.printHomeStatus();

        // 7. Simulate FIRE alert to show emergency shutdown behavior
        System.out.println("\n=== Simulating FIRE alert from Kitchen ===");
        try {
            myHome.broadcastAlert(AlertType.FIRE, "Kitchen", "Fire detected in kitchen!");
        } catch (EmergencyAlertException e) {
            System.out.println("EmergencyAlertException: " + e.getMessage());
            myHome.emergencyShutdown();
        }

        System.out.println("\n=== Final Home Status ===");
        myHome.printHomeStatus();
    }
}
