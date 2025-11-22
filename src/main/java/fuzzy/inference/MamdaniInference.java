package fuzzy.inference;

import fuzzy.operators.*;
import fuzzy.rules.Rule;
import fuzzy.variables.LinguisticVariable;
import fuzzy.variables.FuzzySet;

import java.util.*;

/**
 * Mamdani inference engine.
 * Uses fuzzy sets in the consequent and requires defuzzification.
 */
public class MamdaniInference implements InferenceEngine {
    private final TNorm andOperator;
    private final SNorm orOperator;
    private final ImplicationOperator implicationOperator;
    private final AggregationOperator aggregationOperator;

    public MamdaniInference(TNorm andOperator, SNorm orOperator,
            ImplicationOperator implicationOperator,
            AggregationOperator aggregationOperator) {
        this.andOperator = andOperator;
        this.orOperator = orOperator;
        this.implicationOperator = implicationOperator;
        this.aggregationOperator = aggregationOperator;
    }

    @Override
    public Map<String, Double> infer(List<Rule> rules,
            Map<String, Map<String, Double>> fuzzifiedInputs,
            LinguisticVariable outputVariable) {
        Map<String, List<Double>> outputMemberships = new HashMap<>();

        for (Rule rule : rules) {
            if (!rule.isEnabled()) {
                continue;
            }

            double firingStrength = computeFiringStrength(rule, fuzzifiedInputs);
            firingStrength *= rule.getWeight();

            if (firingStrength > 0) {
                String outputSetName = rule.getConsequentFuzzySetName();
                FuzzySet outputSet = outputVariable.getFuzzySet(outputSetName);

                if (outputSet != null) {
                    outputMemberships.putIfAbsent(outputSetName, new ArrayList<>());
                    outputMemberships.get(outputSetName).add(firingStrength);
                }
            }
        }

        Map<String, Double> result = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : outputMemberships.entrySet()) {
            double aggregated = aggregationOperator.aggregate(
                    entry.getValue().stream().mapToDouble(Double::doubleValue).toArray());
            result.put(entry.getKey(), aggregated);
        }

        return result;
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

    public ImplicationOperator getImplicationOperator() {
        return implicationOperator;
    }
}
