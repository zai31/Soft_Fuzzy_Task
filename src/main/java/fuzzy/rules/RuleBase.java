package fuzzy.rules;

import java.util.*;

/**
 * Manages a collection of fuzzy rules with CRUD operations.
 */
public class RuleBase {
    private final List<Rule> rules;

    public RuleBase() {
        this.rules = new ArrayList<>();
    }

    public void addRule(Rule rule) {
        if (rule == null) {
            throw new IllegalArgumentException("Rule cannot be null");
        }
        rules.add(rule);
    }

    public void removeRule(int index) {
        if (index < 0 || index >= rules.size()) {
            throw new IndexOutOfBoundsException("Invalid rule index");
        }
        rules.remove(index);
    }

    public Rule getRule(int index) {
        if (index < 0 || index >= rules.size()) {
            throw new IndexOutOfBoundsException("Invalid rule index");
        }
        return rules.get(index);
    }

    public List<Rule> getAllRules() {
        return Collections.unmodifiableList(rules);
    }

    public List<Rule> getEnabledRules() {
        List<Rule> enabled = new ArrayList<>();
        for (Rule rule : rules) {
            if (rule.isEnabled()) {
                enabled.add(rule);
            }
        }
        return enabled;
    }

    public int size() {
        return rules.size();
    }

    public void clear() {
        rules.clear();
    }

    public void enableRule(int index) {
        getRule(index).setEnabled(true);
    }

    public void disableRule(int index) {
        getRule(index).setEnabled(false);
    }

    public void setRuleWeight(int index, double weight) {
        getRule(index).setWeight(weight);
    }
}
