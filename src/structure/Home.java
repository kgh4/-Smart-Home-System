package structure;

import core.SmartDevice;
import java.time.LocalTime;
import core.interfaces.EventEmitter;
import enums.AlertType;
import enums.RoomType;
import events.Event;
import events.EventListener;
import exceptions.DeviceNotFoundException;
import exceptions.EmergencyAlertException;
import java.util.ArrayList;
import java.util.List;

public class Home implements EventEmitter {
    private String homeId;
    private String homeName;
    private List<Room> rooms;
    private List<EventListener> eventListeners;
    private LocalTime currentTime;
    
    // Constructor
    public Home(String homeId, String homeName) {
        this.homeId = homeId;
        this.homeName = homeName;
        this.rooms = new ArrayList<>();
        this.eventListeners = new ArrayList<>();
        System.out.println("[Home] Created smart home: " + homeName);
    }
    
    // Add a room to the home
    public void addRoom(Room room) {
        if (room != null) {
            rooms.add(room);
            System.out.println("[Home] Added room '" + room.getRoomName() + "' to " + homeName);
            
            // Register all devices in the room as event listeners
            for (SmartDevice device : room.getDevices()) {
                registerEventListener(device);
            }
        }
    }
    
    // Remove a room from the home
    public void removeRoom(String roomId) {
        Room roomToRemove = null;
        for (Room room : rooms) {
            if (room.getRoomId().equals(roomId)) {
                roomToRemove = room;
                break;
            }
        }
        
        if (roomToRemove != null) {
            rooms.remove(roomToRemove);
            System.out.println("[Home] Removed room '" + roomToRemove.getRoomName() + "' from " + homeName);
        }
    }
    
    // Get a room by its ID
    public Room getRoomById(String roomId) {
        for (Room room : rooms) {
            if (room.getRoomId().equals(roomId)) {
                return room;
            }
        }
        return null;
    }
    
    // Get a room by its type
    public Room getRoomByType(RoomType roomType) {
        for (Room room : rooms) {
            if (room.getRoomType() == roomType) {
                return room;
            }
        }
        return null;
    }
    
    // Get a room by its name
    public Room getRoomByName(String roomName) {
        for (Room room : rooms) {
            if (room.getRoomName().equalsIgnoreCase(roomName)) {
                return room;
            }
        }
        return null;
    }
    
    // Find a device across all rooms
    public SmartDevice findDeviceById(String deviceId) throws DeviceNotFoundException {
        for (Room room : rooms) {
            try {
                return room.getDeviceById(deviceId);
            } catch (DeviceNotFoundException e) {
                // Continue searching in other rooms
            }
        }
        throw new DeviceNotFoundException("Device with ID '" + deviceId + "' not found in any room of " + homeName);
    }
    
    // Find a device by name across all rooms
    public SmartDevice findDeviceByName(String deviceName) throws DeviceNotFoundException {
        for (Room room : rooms) {
            try {
                return room.getDeviceByName(deviceName);
            } catch (DeviceNotFoundException e) {
                // Continue searching in other rooms
            }
        }
        throw new DeviceNotFoundException("Device with name '" + deviceName + "' not found in any room of " + homeName);
    }
    
    // Register an event listener (typically a device)
    public void registerEventListener(EventListener listener) {
        if (listener != null && !eventListeners.contains(listener)) {
            eventListeners.add(listener);
        }
    }
    
    // Unregister an event listener
    public void unregisterEventListener(EventListener listener) {
        eventListeners.remove(listener);
    }
    
    @Override
    public void emitEvent(Event event) {
        System.out.println("\n[Home] Broadcasting event: " + event.getType() + " from " + event.getSource());
        
        // Notify all registered listeners
        for (EventListener listener : eventListeners) {
            listener.onEvent(event);
        }
    }
    
    // Broadcast an alert to all devices
    public void broadcastAlert(AlertType alertType, String source, String message) throws EmergencyAlertException {
        System.out.println("\n========== ALERT ==========");
        System.out.println("Alert Type: " + alertType);
        System.out.println("Source: " + source);
        System.out.println("Message: " + message);
        System.out.println("===========================\n");
        
        // Create and emit the alert event
        Event alertEvent = new Event(alertType.name() + "_ALERT", source, message, null);
        emitEvent(alertEvent);
        
        // For critical alerts, throw an exception
        if (alertType == AlertType.FIRE || alertType == AlertType.EMERGENCY) {
            throw new EmergencyAlertException("CRITICAL ALERT: " + alertType + " - " + message);
        }
    }
    
    // Turn on all devices in the home
    public void turnOnAllDevices() {
        System.out.println("[Home] Turning ON all devices in " + homeName);
        for (Room room : rooms) {
            room.turnOnAllDevices();
        }
    }
    
    // Turn off all devices in the home
    public void turnOffAllDevices() {
        System.out.println("[Home] Turning OFF all devices in " + homeName);
        for (Room room : rooms) {
            room.turnOffAllDevices();
        }
    }
    
    // Get total power usage across all rooms
    public double getTotalPowerUsage() {
        double totalPower = 0.0;
        for (Room room : rooms) {
            totalPower += room.getTotalPowerUsage();
        }
        return totalPower;
    }
    
    // Get total device count across all rooms
    public int getTotalDeviceCount() {
        int count = 0;
        for (Room room : rooms) {
            count += room.getDeviceCount();
        }
        return count;
    }
    
    // Get count of active devices across all rooms
    public int getActiveDeviceCount() {
        int count = 0;
        for (Room room : rooms) {
            count += room.getActiveDeviceCount();
        }
        return count;
    }
    
    // Print status of all rooms
    public void printHomeStatus() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       " + homeName + " STATUS");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("Total Rooms: " + rooms.size());
        System.out.println("Total Devices: " + getTotalDeviceCount());
        System.out.println("Active Devices: " + getActiveDeviceCount());
        System.out.println("Total Power Usage: " + String.format("%.2f", getTotalPowerUsage()) + " W");
        System.out.println("─────────────────────────────────────────");
        
        for (Room room : rooms) {
            room.printRoomStatus();
        }
    }
    
    // List all rooms
    public void listRooms() {
        System.out.println("\n[Home] Rooms in " + homeName + ":");
        if (rooms.isEmpty()) {
            System.out.println("  No rooms");
        } else {
            for (Room room : rooms) {
                System.out.println("  - " + room.getRoomName() + " (" + room.getRoomType() + ") - " 
                    + room.getDeviceCount() + " devices");
            }
        }
    }
    
    // Emergency shutdown
    public void emergencyShutdown() {
        System.out.println("\n[Home] ⚠️ EMERGENCY SHUTDOWN INITIATED ⚠️");
        turnOffAllDevices();
        System.out.println("[Home] All devices have been shut down for safety.");
    }
    
    // Getters
    public String getHomeId() {
        return homeId;
    }
    
    public String getHomeName() {
        return homeName;
    }
    
    public List<Room> getRooms() {
        return new ArrayList<>(rooms); // Return a copy
    }
    
    public List<EventListener> getEventListeners() {
        return new ArrayList<>(eventListeners); // Return a copy
    }
 // ---------- NEW METHOD ----------
 //Returns the current time in the Home.
 //This is used by time-based conditions in automation rules.
    
    public LocalTime getCurrentTime() {
        return currentTime;
    }

    
     //Updates the current time in the Home.
     
     //Can be called by HomeController or AutomationEngine to simulate time progression.
     
    public void setCurrentTime(LocalTime currentTime) {
        this.currentTime = currentTime;
    }
}
