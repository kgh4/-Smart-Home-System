package automation;

import structure.Home;
import java.util.ArrayList;
import java.util.List;

//Manages all automation rules and evaluates them.

public class AutomationEngine {
	private final Home home;
    private final List<AutomationRule> rules;

    public AutomationEngine(Home home) {
        this.home = home;
        this.rules = new ArrayList<>();
    }

    //Add a rule to the engine.
    
    public void addRule(AutomationRule rule) {
        rules.add(rule);
        System.out.println("[AutomationEngine] Rule added: " + rule.getRuleName());
    }

    //Evaluate all rules and execute their actions if conditions are met.
    
    public void run() {
        for (AutomationRule rule : rules) {
            rule.evaluateAndExecute(home);
        }
    }

}
