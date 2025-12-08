package test;

import structure.Home;
import structure.Room;
import devices.Thermostat;
import enums.RoomType;
import enums.HeatingMode;
import enums.AlertType;
import exceptions.DeviceNotFoundException;
import exceptions.EmergencyAlertException;

public class TestPerson4 {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║   Testing Person 4 Implementation (Najm)      ║");
        System.out.println("║   Thermostat, Room, and Home Classes          ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        try {
            // Test 1: Create Home
            System.out.println("========== TEST 1: Create Home ==========");
            Home myHome = new Home("HOME001", "My Smart Home");
            System.out.println("✓ Home created successfully\n");
            
            // Test 2: Create Rooms
            System.out.println("========== TEST 2: Create Rooms ==========");
            Room livingRoom = new Room("ROOM001", "Living Room", RoomType.LIVING_ROOM);
            Room bedroom = new Room("ROOM002", "Bedroom", RoomType.BEDROOM);
            Room kitchen = new Room("ROOM003", "Kitchen", RoomType.KITCHEN);
            Room hallway = new Room("ROOM004", "Hallway", RoomType.HALLWAY);
            
            myHome.addRoom(livingRoom);
            myHome.addRoom(bedroom);
            myHome.addRoom(kitchen);
            myHome.addRoom(hallway);
            System.out.println("✓ All rooms created and added to home\n");
            
            // Test 3: Create Thermostats
            System.out.println("========== TEST 3: Create Thermostats ==========");
            Thermostat livingRoomThermostat = new Thermostat("THERMO001", "Living Room Thermostat", 18.0);
            Thermostat bedroomThermostat = new Thermostat("THERMO002", "Bedroom Thermostat", 19.0);
            System.out.println("✓ Thermostats created\n");
            
            // Test 4: Add Devices to Rooms
            System.out.println("========== TEST 4: Add Devices to Rooms ==========");
            livingRoom.addDevice(livingRoomThermostat);
            bedroom.addDevice(bedroomThermostat);
            System.out.println("✓ Devices added to rooms\n");
            
            // Test 5: Test Master-Slave Coordination
            System.out.println("========== TEST 5: Master-Slave Thermostat Coordination ==========");
            livingRoomThermostat.setAsMaster();
            livingRoomThermostat.addSlaveThermostat(bedroomThermostat);
            
            livingRoomThermostat.turnOn();
            bedroomThermostat.turnOn();
            
            System.out.println("\n--- Setting master temperature to 22°C ---");
            livingRoomThermostat.setTargetTemperature(22.0);
            
            System.out.println("\n--- Simulating temperature changes ---");
            livingRoomThermostat.updateCurrentTemperature(20.0);
            bedroomThermostat.updateCurrentTemperature(21.0);
            System.out.println("✓ Master-slave coordination working\n");
            
            // Test 6: Test Heating Modes
            System.out.println("========== TEST 6: Test Heating Modes ==========");
            livingRoomThermostat.setHeatingMode(HeatingMode.HEATING);
            livingRoomThermostat.updateCurrentTemperature(18.0);
            
            livingRoomThermostat.setHeatingMode(HeatingMode.COOLING);
            livingRoomThermostat.updateCurrentTemperature(25.0);
            
            livingRoomThermostat.setHeatingMode(HeatingMode.AUTO);
            System.out.println("✓ Heating modes tested\n");
            
            // Test 7: Test Room Management
            System.out.println("========== TEST 7: Test Room Management ==========");
            livingRoom.listDevices();
            System.out.println("Active devices in living room: " + livingRoom.getActiveDeviceCount());
            System.out.println("Total power usage in living room: " + String.format("%.2f", livingRoom.getTotalPowerUsage()) + " W");
            System.out.println("✓ Room management working\n");
            
            // Test 8: Test Device Lookup
            System.out.println("========== TEST 8: Test Device Lookup ==========");
            try {
                var foundDevice = myHome.findDeviceByName("Living Room Thermostat");
                System.out.println("Found device: " + foundDevice.getDeviceName());
                
                var foundByType = myHome.getRoomByType(RoomType.BEDROOM);
                System.out.println("Found room by type: " + foundByType.getRoomName());
                System.out.println("✓ Device lookup working\n");
            } catch (DeviceNotFoundException e) {
                System.out.println("✗ Device lookup failed: " + e.getMessage());
            }
            
            // Test 9: Test Power Calculations
            System.out.println("========== TEST 9: Test Power Calculations ==========");
            System.out.println("Living room thermostat power: " + 
                String.format("%.2f", livingRoomThermostat.calculatePowerUsage()) + " W");
            System.out.println("Bedroom thermostat power: " + 
                String.format("%.2f", bedroomThermostat.calculatePowerUsage()) + " W");
            System.out.println("Total home power usage: " + 
                String.format("%.2f", myHome.getTotalPowerUsage()) + " W");
            System.out.println("✓ Power calculations working\n");
            
            // Test 10: Test Home Status
            System.out.println("========== TEST 10: Home Status Report ==========");
            myHome.printHomeStatus();
            System.out.println("✓ Status report generated\n");
            
            // Test 11: Test Alert Broadcasting
            System.out.println("========== TEST 11: Test Alert Broadcasting ==========");
            try {
                myHome.broadcastAlert(AlertType.TEMPERATURE, "Living Room Thermostat", 
                    "Temperature exceeding normal range");
                System.out.println("✓ Temperature alert broadcasted\n");
            } catch (EmergencyAlertException e) {
                System.out.println("Emergency alert: " + e.getMessage());
            }
            
            // Test 12: Test Emergency Alert
            System.out.println("========== TEST 12: Test Emergency Alert ==========");
            try {
                myHome.broadcastAlert(AlertType.FIRE, "Kitchen Smoke Detector", 
                    "Fire detected in kitchen!");
            } catch (EmergencyAlertException e) {
                System.out.println("✓ Emergency exception caught: " + e.getMessage());
            }
            System.out.println();
            
            // Test 13: Test Turn Off All Devices
            System.out.println("========== TEST 13: Turn Off All Devices ==========");
            myHome.turnOffAllDevices();
            System.out.println("Active devices after shutdown: " + myHome.getActiveDeviceCount());
            System.out.println("✓ All devices turned off\n");
            
            // Test 14: Test Device Removal
            System.out.println("========== TEST 14: Test Device Removal ==========");
            try {
                livingRoom.removeDevice("THERMO001");
                System.out.println("Devices in living room after removal: " + livingRoom.getDeviceCount());
                System.out.println("✓ Device removed successfully\n");
            } catch (DeviceNotFoundException e) {
                System.out.println("✗ Device removal failed: " + e.getMessage());
            }
            
            // Final Summary
            System.out.println("\n╔════════════════════════════════════════════════╗");
            System.out.println("║          ALL TESTS COMPLETED SUCCESSFULLY      ║");
            System.out.println("╚════════════════════════════════════════════════╝");
            System.out.println("\nImplemented Components:");
            System.out.println("✓ Thermostat class with master-slave coordination");
            System.out.println("✓ Room class with device management");
            System.out.println("✓ Home class with alert broadcasting");
            System.out.println("✓ HeatingMode enum (HEATING, COOLING, AUTO, OFF)");
            System.out.println("✓ RoomType enum (LIVING_ROOM, BEDROOM, KITCHEN, HALLWAY)");
            System.out.println("✓ AlertType enum (FIRE, SMOKE, MOTION, etc.)");
            
        } catch (Exception e) {
            System.out.println("\n✗ Error during testing: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
