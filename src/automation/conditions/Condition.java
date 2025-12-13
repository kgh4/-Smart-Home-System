package automation.conditions;

import structure.Home;

//Represents a boolean condition for automation rules.

public interface Condition {
	boolean evaluate(Home home);

}
