package automation.actions;

import structure.Home;
import structure.Room;
import enums.RoomType;
import core.SmartDevice;
import devices.SmartTV;

public class TurnOffTVAction implements Action {

    private final RoomType roomType;

    public TurnOffTVAction(RoomType roomType) {
        this.roomType = roomType;
    }

    @Override
    public void execute(Home home) {
        for (Room room : home.getRooms()) {
            if (room.getRoomType() == roomType) {
                for (SmartDevice device : room.getDevices()) {
                    if (device instanceof SmartTV tv) {
                        tv.turnOff();
                    }
                }
            }
        }
    }
}