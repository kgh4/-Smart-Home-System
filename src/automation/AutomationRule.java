package automation;

import structure.Home;
import automation.conditions.Condition;
import automation.actions.Action;
import java.util.List;

//Represents a single automation rule: if condition is true, execute actions.

public class AutomationRule {
	
	 private final Condition condition;
	    private final List<Action> actions;
	    private final String ruleName;

	    public AutomationRule(String ruleName, Condition condition, List<Action> actions) {
	        this.ruleName = ruleName;
	        this.condition = condition;
	        this.actions = actions;
	    }

	    //Evaluates the condition and executes all actions if true.
	    
	    public void evaluateAndExecute(Home home) {
	        if (condition.evaluate(home)) {
	            System.out.println("[AutomationRule] Rule triggered: " + ruleName);
	            for (Action action : actions) {
	                action.execute(home);
	            }
	        }
	    }

	    public String getRuleName() {
	        return ruleName;
	    }

}
