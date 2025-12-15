package automation.conditions;

import structure.Home;
import structure.Room;
import enums.RoomType;
import devices.sensors.SmokeSensor;

//Checks if smoke is detected in a specific room.

public class SmokeDetectedCondition implements Condition {
	private final RoomType roomType;

    public SmokeDetectedCondition(RoomType roomType) {
        this.roomType = roomType;
    }

    @Override
    public boolean evaluate(Home home) {
        for (Room room : home.getRooms()) {
            if (room.getRoomType() == roomType) {
            	// FIX (Person 5): Room does not expose specific device getters.
            	// We search for the SmokeSensor among the room devices.
            	SmokeSensor sensor = null;
            for (core.SmartDevice device : room.getDevices()) {
            	    if (device instanceof SmokeSensor) {
            	        sensor = (SmokeSensor) device;
            	        break;
            	    }
            	}
         // Always check before using it
            if (sensor != null) {
                sensor.updateSmokeLevel(85); // example usage
            }
                return sensor != null && sensor.isSmokeDetected();
            }
        }
        return false;
    }

}
