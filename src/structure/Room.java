package structure;

import core.SmartDevice;
import enums.RoomType;
import exceptions.DeviceNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private String roomId;
    private String roomName;
    private RoomType roomType;
    private List<SmartDevice> devices;
    
    // Constructor
    public Room(String roomId, String roomName, RoomType roomType) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomType = roomType;
        this.devices = new ArrayList<>();
        System.out.println("[Room] Created " + roomName + " (" + roomType + ")");
    }
    
    // Add a device to this room
    public void addDevice(SmartDevice device) {
        if (device != null) {
            devices.add(device);
            System.out.println("[Room] Added device '" + device.getDeviceName() + "' to " + roomName);
        }
    }
    
    // Remove a device from this room
    public void removeDevice(String deviceId) throws DeviceNotFoundException {
        SmartDevice deviceToRemove = null;
        for (SmartDevice device : devices) {
            if (device.getDeviceId().equals(deviceId)) {
                deviceToRemove = device;
                break;
            }
        }
        
        if (deviceToRemove != null) {
            devices.remove(deviceToRemove);
            System.out.println("[Room] Removed device '" + deviceToRemove.getDeviceName() + "' from " + roomName);
        } else {
            throw new DeviceNotFoundException("Device with ID '" + deviceId + "' not found in " + roomName);
        }
    }
    
    // Get a device by its ID
    public SmartDevice getDeviceById(String deviceId) throws DeviceNotFoundException {
        for (SmartDevice device : devices) {
            if (device.getDeviceId().equals(deviceId)) {
                return device;
            }
        }
        throw new DeviceNotFoundException("Device with ID '" + deviceId + "' not found in " + roomName);
    }
    
    // Get a device by its name
    public SmartDevice getDeviceByName(String deviceName) throws DeviceNotFoundException {
        for (SmartDevice device : devices) {
            if (device.getDeviceName().equalsIgnoreCase(deviceName)) {
                return device;
            }
        }
        throw new DeviceNotFoundException("Device with name '" + deviceName + "' not found in " + roomName);
    }
    
    // Turn on all devices in the room
    public void turnOnAllDevices() {
        System.out.println("[Room] Turning ON all devices in " + roomName);
        for (SmartDevice device : devices) {
            device.turnOn();
        }
    }
    
    // Turn off all devices in the room
    public void turnOffAllDevices() {
        System.out.println("[Room] Turning OFF all devices in " + roomName);
        for (SmartDevice device : devices) {
            device.turnOff();
        }
    }
    
    // Get total power usage of all devices in the room
    public double getTotalPowerUsage() {
        double totalPower = 0.0;
        for (SmartDevice device : devices) {
            totalPower += device.calculatePowerUsage();
        }
        return totalPower;
    }
    
    // Get count of active (powered on) devices
    public int getActiveDeviceCount() {
        int count = 0;
        for (SmartDevice device : devices) {
            if (device.isOn()) {
                count++;
            }
        }
        return count;
    }
    
    // List all devices in the room
    public void listDevices() {
        System.out.println("\n[Room] Devices in " + roomName + " (" + roomType + "):");
        if (devices.isEmpty()) {
            System.out.println("  No devices");
        } else {
            for (SmartDevice device : devices) {
                String status = device.isOn() ? "ON" : "OFF";
                System.out.println("  - " + device.getDeviceName() + " [" + device.getDeviceId() + "] - " + status);
            }
        }
    }
    
    // Get room status summary
    public void printRoomStatus() {
        System.out.println("\n========== " + roomName + " Status ==========");
        System.out.println("Room Type: " + roomType);
        System.out.println("Total Devices: " + devices.size());
        System.out.println("Active Devices: " + getActiveDeviceCount());
        System.out.println("Total Power Usage: " + String.format("%.2f", getTotalPowerUsage()) + " W");
        listDevices();
        System.out.println("=====================================\n");
    }
    
    // Getters
    public String getRoomId() {
        return roomId;
    }
    
    public String getRoomName() {
        return roomName;
    }
    
    public RoomType getRoomType() {
        return roomType;
    }
    
    public List<SmartDevice> getDevices() {
        return new ArrayList<>(devices); // Return a copy to prevent external modification
    }
    
    public int getDeviceCount() {
        return devices.size();
    }
}
