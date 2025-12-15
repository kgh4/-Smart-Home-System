package automation.actions;

import structure.Home;
import structure.Room;
import enums.RoomType;
import core.SmartDevice;
import devices.SmartLock;

public class LockDoorAction implements Action {

    private final RoomType roomType;

    public LockDoorAction(RoomType roomType) {
        this.roomType = roomType;
    }

    @Override
    public void execute(Home home) {
        for (Room room : home.getRooms()) {
            if (room.getRoomType() == roomType) {
                for (SmartDevice device : room.getDevices()) {
                    if (device instanceof SmartLock lock) {
                        lock.lock("AUTO");
                    }
                }
            }
        }
    }
}