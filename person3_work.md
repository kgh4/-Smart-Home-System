# Person 3 Implementation Documentation (Roua)

## Overview
Implemented the control devices of the Smart Home System, including Light, SmartTV, and SmartLock, and verified their integration with the existing Home, Room, and Event architecture using a dedicated TestPerson3 scenario.

## Completed Tasks ✓

### 1. Light Class (`devices/Light.java`)

#### Key Features:
- On/off control through SmartDevice and Controllable interface behavior.
- Adjustable brightness (0–100%) and color temperature strings such as "WARM", "NEUTRAL", "COOL".
- Simple scheduling via `executeScheduledTask()` with an auto-off timer (simulated minutes).
- Emergency response to alerts: turns on at maximum brightness on SMOKE or FIRE alerts to illuminate escape paths.

#### Main Methods:
Light(String deviceId, String deviceName, int brightness, String colorTemperature)
void setBrightness(int brightness) // Validate and set brightness (0–100)
void setColorTemperature(String mode) // Update color temperature
void scheduleAutoOff(int minutes) // Configure auto-off timer
double calculatePowerUsage() // Power proportional to brightness if ON
void executeScheduledTask() // Apply auto-off if configured
void onEvent(Event event) // React to FIRE/SMOKE/TEMPERATURE alerts
void performAction() // Toggle light state

text

### 2. SmartTV Class (`devices/SmartTV.java`)

#### Key Features:
- Channel and volume management with validation, plus mute/unmute control.
- Power usage calculation that increases slightly with higher volume when turned on.
- Scheduled task support to automatically turn the TV off (e.g., evening rule).
- Emergency shutdown behavior: on FIRE, SMOKE, or EMERGENCY alerts, the TV turns off and enters an `emergencyShutdown` state to prevent accidental re-activation.

#### Main Methods:
SmartTV(String deviceId, String deviceName, int initialChannel, int initialVolume)
void setChannel(int channel) // Change current channel
void setVolume(int volume) // Adjust volume (0–100)
void mute()
void unmute()
double calculatePowerUsage() // Base 80W scaled by volume factor
void executeScheduledTask() // Auto power-off behavior
void onEvent(Event event) // Handle FIRE/SMOKE/EMERGENCY/MOTION alerts
void performAction() // Switch to default news channel and power on

text

### 3. SmartLock Class (`devices/SmartLock.java`)

#### Key Features:
- Lock/unlock operations with a boolean `locked` state and an access log of operations including actor information.
- Low constant power usage when powered, returned by `calculatePowerUsage()`.
- Scheduled task support to auto-lock if the door is left unlocked.
- Emergency reaction: automatically locks when FIRE, SMOKE, or EMERGENCY alerts are broadcast.

#### Main Methods:
SmartLock(String deviceId, String deviceName)
boolean isLocked() // Query lock state
void lock(String actor) // Lock and log actor
void unlock(String actor) // Unlock and log actor
List<String> getAccessLog() // Defensive copy of log
double calculatePowerUsage() // Constant low power when ON
void executeScheduledTask() // Auto-lock if unlocked
void onEvent(Event event) // Lock on emergency alerts
void performAction() // Toggle lock by "UserAction"

text

### 4. Test Class (`test/TestPerson3.java`)

#### Purpose:
Demonstrates that Person 3 devices integrate with the shared system and satisfy the required behaviors under normal operation, scheduling, and emergency alerts.

#### Scenario Steps:
// Home and rooms
Home myHome = new Home("HOME001", "Person3 Demo Home");
Room livingRoom = new Room("ROOM001", "Living Room", RoomType.LIVING_ROOM);
Room bedroom = new Room("ROOM002", "Bedroom", RoomType.BEDROOM);
Room kitchen = new Room("ROOM003", "Kitchen", RoomType.KITCHEN);
myHome.addRoom(livingRoom);
myHome.addRoom(bedroom);
myHome.addRoom(kitchen);

// Person 3 devices
Light livingRoomLight = new Light("LIGHT001", "Living Room Main Light", 60, "WARM");
SmartTV livingRoomTV = new SmartTV("TV001", "Living Room TV", 5, 30);
SmartLock bedroomLock = new SmartLock("LOCK001", "Bedroom Door Lock");

// Add to rooms and register for events
livingRoom.addDevice(livingRoomLight);
livingRoom.addDevice(livingRoomTV);
bedroom.addDevice(bedroomLock);
myHome.registerEventListener(livingRoomLight);
myHome.registerEventListener(livingRoomTV);
myHome.registerEventListener(bedroomLock);

// Control, scheduling, and alerts
myHome.turnOnAllDevices();
livingRoomLight.setBrightness(80);
livingRoomLight.setColorTemperature("NEUTRAL");
livingRoomLight.scheduleAutoOff(5);
livingRoomTV.setChannel(10);
livingRoomTV.setVolume(40);
myHome.printHomeStatus();

livingRoomLight.executeScheduledTask();
livingRoomTV.executeScheduledTask();
bedroomLock.executeScheduledTask();
myHome.printHomeStatus();

myHome.broadcastAlert(AlertType.SMOKE, "Kitchen", "Smoke detected in kitchen!");
myHome.printHomeStatus();

try {
myHome.broadcastAlert(AlertType.FIRE, "Kitchen", "Fire detected in kitchen!");
} catch (EmergencyAlertException e) {
myHome.emergencyShutdown();
}
myHome.printHomeStatus();

text

## Architecture Integration

### With Person 1 (Core Architecture)
- All devices extend the abstract `SmartDevice` and implement the required methods `calculatePowerUsage`, `executeScheduledTask`, `onEvent`, and `performAction`, honoring the `Controllable`, `EnergyConsumer`, `Schedulable`, and `EventListener` interfaces.  
- Devices use `Event` objects and `AlertType` enums consistently with the event system design. 

### With Person 4 (Home/Room/Thermostat)
- Devices are stored in `Room` collections and contribute to `getTotalPowerUsage`, `getActiveDeviceCount`, and `printRoomStatus`.   
- Devices register as `EventListener`s in `Home` and respond correctly when `Home.broadcastAlert` emits alerts; emergency alerts work together with `EmergencyAlertException` and `Home.emergencyShutdown()`. 

## Testing Results ✓
The `TestPerson3` scenario confirms:
- ✓ Home and room creation using existing structure.  
- ✓ Creation and room assignment of Light, SmartTV, and SmartLock devices.   
- ✓ Correct on/off behavior via `Home.turnOnAllDevices` and scheduled tasks.
- ✓ Power usage reporting at device, room, and home levels with realistic values. 
- ✓ SMOKE alert: light turns on to max brightness, TV shuts down, lock secures the bedroom door. 
- ✓ FIRE alert: same emergency actions plus global emergency shutdown and zero final power usage. 

**Implementation Status**: ✅ COMPLETE  
**Test Status**: ✅ PERSON 3 SCENARIO PASSING  
**Ready for Integration**: ✅ YES (devices integrate cleanly with Persons 1, 2, 4, and 5 components)