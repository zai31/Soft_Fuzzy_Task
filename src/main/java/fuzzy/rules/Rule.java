package fuzzy.rules;

import java.util.*;

/**
 * Represents a fuzzy IF-THEN rule.
 * Antecedent can have multiple conditions connected by AND/OR.
 */
public class Rule {
    public static class AntecedentCondition {
        private final String variableName;
        private final String fuzzySetName;
        private final boolean isAnd; // true for AND, false for OR

        public AntecedentCondition(String variableName, String fuzzySetName, boolean isAnd) {
            this.variableName = variableName;
            this.fuzzySetName = fuzzySetName;
            this.isAnd = isAnd;
        }

        public String getVariableName() {
            return variableName;
        }

        public String getFuzzySetName() {
            return fuzzySetName;
        }

        public boolean isAnd() {
            return isAnd;
        }
    }

    private final List<AntecedentCondition> antecedent;
    private final String consequentVariableName;
    private final String consequentFuzzySetName;
    private boolean enabled;
    private double weight;

    public Rule(String consequentVariableName, String consequentFuzzySetName) {
        this.antecedent = new ArrayList<>();
        this.consequentVariableName = consequentVariableName;
        this.consequentFuzzySetName = consequentFuzzySetName;
        this.enabled = true;
        this.weight = 1.0;
    }

    public void addAntecedentCondition(String variableName, String fuzzySetName, boolean isAnd) {
        antecedent.add(new AntecedentCondition(variableName, fuzzySetName, isAnd));
    }

    public List<AntecedentCondition> getAntecedent() {
        return Collections.unmodifiableList(antecedent);
    }

    public String getConsequentVariableName() {
        return consequentVariableName;
    }

    public String getConsequentFuzzySetName() {
        return consequentFuzzySetName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight < 0 || weight > 1) {
            throw new IllegalArgumentException("Rule weight must be in [0, 1]");
        }
        this.weight = weight;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("IF ");
        for (int i = 0; i < antecedent.size(); i++) {
            AntecedentCondition cond = antecedent.get(i);
            if (i > 0) {
                sb.append(cond.isAnd() ? " AND " : " OR ");
            }
            sb.append(cond.getVariableName()).append(" IS ").append(cond.getFuzzySetName());
        }
        sb.append(" THEN ").append(consequentVariableName).append(" IS ").append(consequentFuzzySetName);
        if (weight != 1.0) {
            sb.append(" (weight: ").append(weight).append(")");
        }
        return sb.toString();
    }
}
