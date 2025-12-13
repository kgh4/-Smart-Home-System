package automation.actions;


import structure.Home;
import structure.Room;
import enums.RoomType;
import core.SmartDevice;
import devices.Light; 


public class TurnOnLightAction implements Action {

    private final RoomType roomType;

    public TurnOnLightAction(RoomType roomType) {
        this.roomType = roomType;
    }

    @Override
    public void execute(Home home) {
        for (Room room : home.getRooms()) {
            if (room.getRoomType() == roomType) {
                for (SmartDevice device : room.getDevices()) {
                    if (device instanceof Light light) {
                        light.turnOn();
                    }
                }
            }
        }
    }
}