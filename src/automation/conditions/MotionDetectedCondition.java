package automation.conditions;

import structure.Home;
import structure.Room;
import enums.RoomType;
import devices.sensors.MotionSensor;

//Checks if motion is detected in a specific room.

public class MotionDetectedCondition implements Condition{
	 private final RoomType roomType;

	    public MotionDetectedCondition(RoomType roomType) {
	        this.roomType = roomType;
	    }

	    @Override
	    public boolean evaluate(Home home) {
	        for (Room room : home.getRooms()) {
	            if (room.getRoomType() == roomType) {
	            	MotionSensor sensor = null;
	            	for (core.SmartDevice device : room.getDevices()) {
	            	    if (device instanceof MotionSensor) {
	            	        sensor = (MotionSensor) device;
	            	        break;
	            	    }
	            	}

	            	// Always check before using it
	            	if (sensor != null) {
	            	    sensor.detectMotion(); // example usage
	            	}
	                return sensor != null && sensor.isMotionDetected();
	            }
	        }
	        return false;
	    
	    }

}
