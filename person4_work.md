# Person 4 Implementation Documentation (Najm)

## Overview
Successfully implemented the structural components of the Smart Home System, including Thermostat with master-slave coordination, Room management, and Home control system.

## Completed Tasks ✓

### 1. Enum Classes (Completed Person 1's work)
- **RoomType.java** - Enum for different room types
  - LIVING_ROOM, BEDROOM, KITCHEN, HALLWAY
  
- **HeatingMode.java** - Enum for thermostat modes
  - HEATING, COOLING, AUTO, OFF
  
- **AlertType.java** - Enum for system alerts
  - FIRE, SMOKE, MOTION, INTRUSION, TEMPERATURE, EMERGENCY

### 2. Exception Classes (Completed Person 1's work)
- **DeviceNotFoundException.java** - Thrown when a device cannot be found
- **EmergencyAlertException.java** - Thrown for critical emergency situations
- **InvalidOperationException.java** - Thrown for invalid operations

### 3. Thermostat Class (`devices/Thermostat.java`)

#### Key Features:
- **Master-Slave Coordination**: One master thermostat can control multiple slave thermostats
- **Temperature Control**: Set target temperatures and monitor current temperatures
- **Multiple Heating Modes**: HEATING, COOLING, AUTO, OFF
- **Power Calculation**: Calculates power usage based on temperature difference
- **Event Response**: Responds to emergency events (smoke, fire) by shutting down

#### Main Methods:
```java
void setAsMaster()                          // Designate as master thermostat
void addSlaveThermostat(Thermostat slave)   // Link slave to master
void setTargetTemperature(double temp)      // Set target temp (syncs slaves)
void setHeatingMode(HeatingMode mode)       // Set heating mode
void updateCurrentTemperature(double temp)  // Update current temperature
double calculatePowerUsage()                // Calculate power consumption
```

#### Master-Slave Behavior:
- When master's target temperature changes, all slaves automatically sync
- Slaves follow master's temperature settings
- Each thermostat can still operate independently when not linked

### 4. Room Class (`structure/Room.java`)

#### Key Features:
- **Device Management**: Add, remove, and find devices in the room
- **Power Monitoring**: Track total power usage of all devices
- **Bulk Operations**: Turn all devices on/off at once
- **Status Reporting**: Display detailed room status

#### Main Methods:
```java
void addDevice(SmartDevice device)                  // Add device to room
void removeDevice(String deviceId)                  // Remove device from room
SmartDevice getDeviceById(String deviceId)          // Find device by ID
SmartDevice getDeviceByName(String deviceName)      // Find device by name
void turnOnAllDevices()                             // Turn on all devices
void turnOffAllDevices()                            // Turn off all devices
double getTotalPowerUsage()                         // Get total power consumption
int getActiveDeviceCount()                          // Count active devices
void printRoomStatus()                              // Display room status
```

### 5. Home Class (`structure/Home.java`)

#### Key Features:
- **Room Management**: Add, remove, and search for rooms
- **Global Device Search**: Find devices across all rooms
- **Event Broadcasting**: Implements EventEmitter interface
- **Alert System**: Broadcast alerts to all devices
- **Emergency Response**: Trigger emergency shutdowns
- **Comprehensive Monitoring**: Track power usage and device status across entire home

#### Main Methods:
```java
void addRoom(Room room)                                 // Add room to home
Room getRoomById(String roomId)                         // Find room by ID
Room getRoomByType(RoomType roomType)                   // Find room by type
Room getRoomByName(String roomName)                     // Find room by name
SmartDevice findDeviceById(String deviceId)             // Find device anywhere
SmartDevice findDeviceByName(String deviceName)         // Find device by name
void emitEvent(Event event)                             // Broadcast event to all listeners
void broadcastAlert(AlertType type, String source, msg) // Send alerts
void turnOnAllDevices()                                 // Turn on all devices in home
void turnOffAllDevices()                                // Turn off all devices in home
double getTotalPowerUsage()                             // Get total home power usage
void emergencyShutdown()                                // Emergency shutdown all devices
void printHomeStatus()                                  // Display comprehensive status
```

## Architecture Integration

### Event System Integration
- Home implements `EventEmitter` interface
- All devices in rooms are automatically registered as event listeners
- Events are broadcast to all registered listeners
- Thermostats respond to temperature and emergency events

### Exception Handling
- `DeviceNotFoundException` - Used when searching for devices that don't exist
- `EmergencyAlertException` - Thrown for FIRE and EMERGENCY alerts
- Proper try-catch blocks in test code demonstrate usage

### Power Monitoring
- Each device calculates its own power usage
- Room aggregates power usage of all its devices
- Home provides total power consumption across all rooms
- Thermostat power calculation based on temperature differential

## Testing Results ✓

All 14 tests passed successfully:
1. ✓ Home creation
2. ✓ Room creation (4 rooms)
3. ✓ Thermostat creation
4. ✓ Device addition to rooms
5. ✓ Master-slave thermostat coordination
6. ✓ Heating mode changes (HEATING, COOLING, AUTO)
7. ✓ Room management operations
8. ✓ Device lookup across home
9. ✓ Power usage calculations
10. ✓ Home status reporting
11. ✓ Alert broadcasting (non-critical)
12. ✓ Emergency alert handling
13. ✓ Turn off all devices
14. ✓ Device removal

## Usage Example

```java
// Create home and rooms
Home myHome = new Home("HOME001", "My Smart Home");
Room livingRoom = new Room("ROOM001", "Living Room", RoomType.LIVING_ROOM);
Room bedroom = new Room("ROOM002", "Bedroom", RoomType.BEDROOM);

// Add rooms to home
myHome.addRoom(livingRoom);
myHome.addRoom(bedroom);

// Create thermostats
Thermostat masterThermo = new Thermostat("THERMO001", "Living Room Thermostat", 18.0);
Thermostat slaveThermo = new Thermostat("THERMO002", "Bedroom Thermostat", 19.0);

// Add devices to rooms
livingRoom.addDevice(masterThermo);
bedroom.addDevice(slaveThermo);

// Setup master-slave coordination
masterThermo.setAsMaster();
masterThermo.addSlaveThermostat(slaveThermo);

// Turn on and set temperature (will sync to slave)
masterThermo.turnOn();
slaveThermo.turnOn();
masterThermo.setTargetTemperature(22.0); // Bedroom will also update to 22°C

// Monitor and control
myHome.printHomeStatus();
System.out.println("Total power: " + myHome.getTotalPowerUsage() + "W");

// Emergency alert
myHome.broadcastAlert(AlertType.FIRE, "Kitchen", "Fire detected!");
```

## Dependencies Ready for Other Team Members

### For Person 2 (Sensors & Alerts):
- ✓ Home class with `broadcastAlert()` method ready
- ✓ AlertType enum with FIRE, SMOKE, MOTION types
- ✓ Event system integration ready
- ✓ EmergencyAlertException ready

### For Person 3 (Control Devices):
- ✓ Room structure ready to receive Light, SmartTV, SmartLock devices
- ✓ SmartDevice base class provided by Person 1
- ✓ Event listener system ready

### For Person 5 (Automation & Controller):
- ✓ Home class ready with device lookup methods
- ✓ Room management structure in place
- ✓ Event emission system ready
- ✓ All enums and exceptions ready

## File Structure

```
src/
├── devices/
│   └── Thermostat.java          (Person 4)
├── structure/
│   ├── Room.java                (Person 4)
│   └── Home.java                (Person 4)
├── enums/
│   ├── RoomType.java            (Person 4 - completed)
│   ├── HeatingMode.java         (Person 4 - completed)
│   └── AlertType.java           (Person 4 - completed)
├── exceptions/
│   ├── DeviceNotFoundException.java      (Person 4 - completed)
│   ├── EmergencyAlertException.java      (Person 4 - completed)
│   └── InvalidOperationException.java    (Person 4 - completed)
└── test/
    └── TestPerson4.java         (Person 4)
```

## Next Steps for Integration

1. **Person 2** can now implement sensors that use `Home.broadcastAlert()`
2. **Person 3** can create devices that will be added to rooms
3. **Person 5** can use Home and Room classes for the controller
4. All team members can reference the working test file for examples

## Notes

- All code follows the established architecture from Person 1
- Proper encapsulation with getters and defensive copying
- Comprehensive error handling with custom exceptions
- Detailed console output for debugging and demonstration
- Power calculations are realistic and scale with usage
- Master-slave pattern allows for realistic smart home scenarios

---
**Implementation Status**: ✅ COMPLETE
**Test Status**: ✅ ALL TESTS PASSING
**Ready for Integration**: ✅ YES
