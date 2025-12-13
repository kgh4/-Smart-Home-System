package automation.conditions;

import structure.Home;
import java.time.LocalTime;

//Checks if the current time in the Home is after a specified time.

public class TimeAfterCondition implements Condition{
	private final LocalTime time;

    public TimeAfterCondition(LocalTime time) {
        this.time = time;
    }

    @Override
    public boolean evaluate(Home home) {
        return home.getCurrentTime().isAfter(time);
    }

}
