package automation;

import structure.Home;
import enums.RoomType;
import java.time.LocalTime;

public class HomeController {
	
	private final Home home;
    private final AutomationEngine engine;

    public HomeController(Home home) {
        this.home = home;
        this.engine = new AutomationEngine(home);
    }

    // Add a rule to the engine
    public void addRule(AutomationRule rule) {
        engine.addRule(rule);
    }

    // Run all automation rules
    public void runAutomation() {
        engine.run();
    }

    // Update simulated current time
    public void updateTime(LocalTime time) {
        home.setCurrentTime(time);
    }

    // Turn on all devices in a room
    public void turnOnRoom(RoomType roomType) {
        home.getRooms().stream()
            .filter(r -> r.getRoomType() == roomType)
            .forEach(r -> r.turnOnAllDevices());
    }

    // Turn off all devices in a room
    public void turnOffRoom(RoomType roomType) {
        home.getRooms().stream()
            .filter(r -> r.getRoomType() == roomType)
            .forEach(r -> r.turnOffAllDevices());
    }

    // Emergency shutdown
    public void emergencyShutdown() {
        home.emergencyShutdown();
    }

    // Access to Home (if needed)
    public Home getHome() {
        return home;
    }
	

}
