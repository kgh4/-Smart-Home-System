package app;

import structure.Home;
import structure.Room;
import enums.RoomType;
import automation.HomeController;
import automation.AutomationRule;
import automation.conditions.*;
import automation.conditions.CompositeCondition.Operator;
import automation.actions.*;
import devices.Light;
import devices.SmartTV;
import devices.SmartLock;
import devices.sensors.MotionSensor;
import devices.sensors.SmokeSensor;
import java.time.LocalTime;
import java.util.Arrays;         

public class Main {
    public static void main(String[] args) {
        // ---------- Create Home ----------
        Home home = new Home("H1", "MySmartHome");

        // ---------- Add Rooms ----------
        Room livingRoom = new Room("R1", "Living Room", RoomType.LIVING_ROOM);
        Room hallway = new Room("R2", "Hallway", RoomType.HALLWAY);
        Room bedroom = new Room("R3", "Bedroom", RoomType.BEDROOM);
        Room kitchen = new Room("R4", "Kitchen", RoomType.KITCHEN);

        home.addRoom(livingRoom);
        home.addRoom(hallway);
        home.addRoom(bedroom);
        home.addRoom(kitchen);

        // ---------- Add Devices to Rooms ----------

        // Living Room
        Light livingRoomLight = new Light("L1", "Living Room Light", 80, "NEUTRAL");
        SmartTV livingRoomTV = new SmartTV("TV1", "Living Room TV", 1, 20);
        livingRoom.addDevice(livingRoomLight);
        livingRoom.addDevice(livingRoomTV);
        home.registerEventListener(livingRoomLight);
        home.registerEventListener(livingRoomTV);

        // Hallway
        Light hallwayLight = new Light("L2", "Hallway Light", 100, "WARM");
        MotionSensor hallwaySensor = new MotionSensor("MS1", "Hallway Motion", 5, home);
        hallway.addDevice(hallwayLight);
        hallway.addDevice(hallwaySensor);
        home.registerEventListener(hallwayLight);
        home.registerEventListener(hallwaySensor);

        // Bedroom
        Light bedroomLight = new Light("L3", "Bedroom Light", 60, "COOL");
        SmartLock bedroomLock = new SmartLock("LOCK1", "Bedroom Lock");
        bedroom.addDevice(bedroomLight);
        bedroom.addDevice(bedroomLock);
        home.registerEventListener(bedroomLight);
        home.registerEventListener(bedroomLock);

        // Kitchen
        Light kitchenLight = new Light("L4", "Kitchen Light", 70, "NEUTRAL");
        SmokeSensor kitchenSensor = new SmokeSensor("SS1", "Kitchen Smoke Sensor", 70, home);
        kitchen.addDevice(kitchenLight);
        kitchen.addDevice(kitchenSensor);
        home.registerEventListener(kitchenLight);
        home.registerEventListener(kitchenSensor);

        // ---------- Initialize HomeController ----------
        HomeController controller = new HomeController(home);

        // ---------- Demo Scenario 1: Motion in Hallway ----------
        MotionDetectedCondition motionHallway = new MotionDetectedCondition(RoomType.HALLWAY);
        TurnOnLightAction turnOnHallwayLight = new TurnOnLightAction(RoomType.HALLWAY);

        AutomationRule motionRule = new AutomationRule(
            "Hallway Motion Rule",
            motionHallway,
            Arrays.asList(turnOnHallwayLight)
        );
        controller.addRule(motionRule);

        // ---------- Demo Scenario 2: Smoke detected ----------
        SmokeDetectedCondition smokeKitchen = new SmokeDetectedCondition(RoomType.KITCHEN);
        TurnOffTVAction turnOffLivingTV = new TurnOffTVAction(RoomType.LIVING_ROOM);
        LockDoorAction lockBedroomDoor = new LockDoorAction(RoomType.BEDROOM);

        AutomationRule smokeRule = new AutomationRule(
            "Kitchen Smoke Rule",
            smokeKitchen,
            Arrays.asList(turnOffLivingTV, lockBedroomDoor)
        );
        controller.addRule(smokeRule);

        // ---------- Demo Scenario 3: Evening Rule (10 PM + no motion) ----------
        TimeAfterCondition after10PM = new TimeAfterCondition(LocalTime.of(22, 0));
        CompositeCondition eveningCondition = new CompositeCondition(
            Arrays.asList(motionHallway, after10PM),
            Operator.AND
        );
        TurnOffTVAction turnOffTVEvening = new TurnOffTVAction(RoomType.LIVING_ROOM);
        TurnOnLightAction dimLights = new TurnOnLightAction(RoomType.LIVING_ROOM); // could adjust brightness if supported

        AutomationRule eveningRule = new AutomationRule(
            "Evening Rule",
            eveningCondition,
            Arrays.asList(turnOffTVEvening, dimLights)
        );
        controller.addRule(eveningRule);

        // ---------- Simulate time progression ----------
        controller.updateTime(LocalTime.of(22, 5)); // 10:05 PM

        // ---------- Run Automation ----------
        controller.runAutomation();

        // ---------- Print Home Status ----------
        home.printHomeStatus();
    }
}
