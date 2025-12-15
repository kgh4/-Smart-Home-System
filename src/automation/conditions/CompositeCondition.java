package automation.conditions;

import structure.Home;
import java.util.List;

//Combines multiple conditions using AND or OR logic.

public class CompositeCondition implements Condition{
	public enum Operator { AND, OR }

    private final List<Condition> conditions;
    private final Operator operator;

    public CompositeCondition(List<Condition> conditions, Operator operator) {
        this.conditions = conditions;
        this.operator = operator;
    }

    @Override
    public boolean evaluate(Home home) {
        if (operator == Operator.AND) {
            for (Condition c : conditions) {
                if (!c.evaluate(home)) return false;
            }
            return true;
        } else { // OR
            for (Condition c : conditions) {
                if (c.evaluate(home)) return true;
            }
            return false;
        }
    }

}
