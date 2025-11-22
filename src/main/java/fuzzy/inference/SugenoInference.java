package fuzzy.inference;

import fuzzy.operators.*;
import fuzzy.rules.Rule;
import fuzzy.variables.LinguisticVariable;

import java.util.*;

/**
 * Sugeno inference engine (zero-order or first-order).
 * Uses crisp values in the consequent.
 */
public class SugenoInference implements InferenceEngine {
    private final TNorm andOperator;
    private final SNorm orOperator;
    private final Map<String, Double> consequentValues; // fuzzy set name -> crisp value
    private final boolean isFirstOrder;
    private final Map<String, Map<String, Double>> consequentCoefficients; // for first-order

    public SugenoInference(TNorm andOperator, SNorm orOperator,
            Map<String, Double> consequentValues) {
        this(andOperator, orOperator, consequentValues, false, null);
    }

    public SugenoInference(TNorm andOperator, SNorm orOperator,
            Map<String, Double> consequentValues,
            boolean isFirstOrder,
            Map<String, Map<String, Double>> consequentCoefficients) {
        this.andOperator = andOperator;
        this.orOperator = orOperator;
        this.consequentValues = consequentValues;
        this.isFirstOrder = isFirstOrder;
        this.consequentCoefficients = consequentCoefficients;
    }

    @Override
    public Map<String, Double> infer(List<Rule> rules,
            Map<String, Map<String, Double>> fuzzifiedInputs,
            LinguisticVariable outputVariable) {
        double weightedSum = 0.0;
        double weightSum = 0.0;

        for (Rule rule : rules) {
            if (!rule.isEnabled()) {
                continue;
            }

            double firingStrength = computeFiringStrength(rule, fuzzifiedInputs);
            firingStrength *= rule.getWeight();

            if (firingStrength > 0) {
                String outputSetName = rule.getConsequentFuzzySetName();
                double consequentValue = computeConsequentValue(outputSetName, fuzzifiedInputs);

                weightedSum += firingStrength * consequentValue;
                weightSum += firingStrength;
            }
        }

        Map<String, Double> result = new HashMap<>();
        if (weightSum > 0) {
            result.put("output", weightedSum / weightSum);
        } else {
            result.put("output", 0.0);
        }

        return result;
    }

    private double computeConsequentValue(String fuzzySetName,
            Map<String, Map<String, Double>> fuzzifiedInputs) {
        if (!isFirstOrder) {
            return consequentValues.getOrDefault(fuzzySetName, 0.0);
        }

        Map<String, Double> coefficients = consequentCoefficients.get(fuzzySetName);
        if (coefficients == null) {
            return consequentValues.getOrDefault(fuzzySetName, 0.0);
        }

        double value = consequentValues.getOrDefault(fuzzySetName, 0.0);
        for (Map.Entry<String, Double> entry : coefficients.entrySet()) {
            String varName = entry.getKey();
            Map<String, Double> varInputs = fuzzifiedInputs.get(varName);
            if (varInputs != null) {
                double crispValue = getCrispValue(varName, varInputs);
                value += entry.getValue() * crispValue;
            }
        }
        return value;
    }

    private double getCrispValue(String varName, Map<String, Double> fuzzyMemberships) {
        double weightedSum = 0.0;
        double weightSum = 0.0;
        for (Map.Entry<String, Double> entry : fuzzyMemberships.entrySet()) {
            double membership = entry.getValue();
            double center = consequentValues.getOrDefault(entry.getKey(), 0.0);
            weightedSum += membership * center;
            weightSum += membership;
        }
        return weightSum > 0 ? weightedSum / weightSum : 0.0;
    }

    private double getFuzzySetCenter(String varName, Map<String, Double> fuzzyMemberships) {
        double maxMembership = 0.0;
        String maxSet = null;
        for (Map.Entry<String, Double> entry : fuzzyMemberships.entrySet()) {
            if (entry.getValue() > maxMembership) {
                maxMembership = entry.getValue();
                maxSet = entry.getKey();
            }
        }
        return maxSet != null ? consequentValues.getOrDefault(maxSet, 0.0) : 0.0;
    }

    private double computeFiringStrength(Rule rule, Map<String, Map<String, Double>> fuzzifiedInputs) {
        List<Rule.AntecedentCondition> antecedent = rule.getAntecedent();
        if (antecedent.isEmpty()) {
            return 1.0;
        }

        double result = getMembership(antecedent.get(0), fuzzifiedInputs);

        for (int i = 1; i < antecedent.size(); i++) {
            Rule.AntecedentCondition cond = antecedent.get(i);
            double membership = getMembership(cond, fuzzifiedInputs);

            if (cond.isAnd()) {
                result = andOperator.compute(result, membership);
            } else {
                result = orOperator.compute(result, membership);
            }
        }

        return result;
    }

    private double getMembership(Rule.AntecedentCondition cond,
            Map<String, Map<String, Double>> fuzzifiedInputs) {
        Map<String, Double> variableInputs = fuzzifiedInputs.get(cond.getVariableName());
        if (variableInputs == null) {
            return 0.0;
        }
        return variableInputs.getOrDefault(cond.getFuzzySetName(), 0.0);
    }
}
