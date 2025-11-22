package fuzzy.system;

import fuzzy.defuzzification.*;
import fuzzy.inference.*;
import fuzzy.operators.*;
import fuzzy.rules.*;
import fuzzy.variables.*;

import java.util.*;

/**
 * Main fuzzy logic system that orchestrates the complete evaluation pipeline:
 * fuzzify → infer → aggregate → defuzzify → crisp output
 */
public class FuzzyLogicSystem {
    private final Map<String, LinguisticVariable> inputVariables;
    private final LinguisticVariable outputVariable;
    private final RuleBase ruleBase;
    private InferenceEngine inferenceEngine;
    private DefuzzificationMethod defuzzificationMethod;
    private TNorm andOperator;
    private SNorm orOperator;

    public FuzzyLogicSystem(LinguisticVariable outputVariable) {
        this.inputVariables = new LinkedHashMap<>();
        this.outputVariable = outputVariable;
        this.ruleBase = new RuleBase();

        setDefaultOperators();
    }

    private void setDefaultOperators() {
        this.andOperator = new MinTNorm();
        this.orOperator = new MaxSNorm();
        this.defuzzificationMethod = new CentroidDefuzzification();

        if (inferenceEngine == null) {
            this.inferenceEngine = new MamdaniInference(
                    andOperator, orOperator,
                    new MinImplication(),
                    new MaxAggregation());
        }
    }

    public void addInputVariable(LinguisticVariable variable) {
        if (variable == null) {
            throw new IllegalArgumentException("Input variable cannot be null");
        }
        inputVariables.put(variable.getName(), variable);
    }

    public LinguisticVariable getInputVariable(String name) {
        return inputVariables.get(name);
    }

    public LinguisticVariable getOutputVariable() {
        return outputVariable;
    }

    public RuleBase getRuleBase() {
        return ruleBase;
    }

    public void setInferenceEngine(InferenceEngine engine) {
        this.inferenceEngine = engine;
    }

    public void setDefuzzificationMethod(DefuzzificationMethod method) {
        this.defuzzificationMethod = method;
    }

    public void setAndOperator(TNorm operator) {
        this.andOperator = operator;
        updateInferenceEngine();
    }

    public void setOrOperator(SNorm operator) {
        this.orOperator = operator;
        updateInferenceEngine();
    }

    private void updateInferenceEngine() {
        if (inferenceEngine instanceof MamdaniInference) {
            this.inferenceEngine = new MamdaniInference(
                    andOperator, orOperator,
                    new MinImplication(),
                    new MaxAggregation());
        } else if (inferenceEngine instanceof SugenoInference) {
            SugenoInference sugeno = (SugenoInference) inferenceEngine;
            Map<String, Double> consequentValues = extractConsequentValues(sugeno);
            this.inferenceEngine = new SugenoInference(
                    andOperator, orOperator,
                    consequentValues);
        }
    }

    private Map<String, Double> extractConsequentValues(SugenoInference sugeno) {
        Map<String, Double> values = new HashMap<>();
        for (FuzzySet fuzzySet : outputVariable.getAllFuzzySets()) {
            double[] domain = fuzzySet.getMembershipFunction().getDomain();
            double center = (domain[0] + domain[1]) / 2.0;
            values.put(fuzzySet.getName(), center);
        }
        return values;
    }

    public void setupSugenoInference(Map<String, Double> consequentValues) {
        this.inferenceEngine = new SugenoInference(
                andOperator, orOperator, consequentValues);
    }

    /**
     * Complete evaluation pipeline: fuzzify → infer → aggregate → defuzzify
     * 
     * @param crispInputs map of input variable names to crisp values
     * @return crisp output value
     */
    public double evaluate(Map<String, Double> crispInputs) {
        Map<String, Map<String, Double>> fuzzifiedInputs = fuzzify(crispInputs);
        Map<String, Double> inferredOutput = inferenceEngine.infer(
                ruleBase.getEnabledRules(), fuzzifiedInputs, outputVariable);
        return defuzzify(inferredOutput);
    }

    /**
     * Fuzzification step: converts crisp inputs to membership degrees.
     * 
     * @param crispInputs map of input variable names to crisp values
     * @return map of variable names to fuzzy set names to membership degrees
     */
    public Map<String, Map<String, Double>> fuzzify(Map<String, Double> crispInputs) {
        Map<String, Map<String, Double>> fuzzified = new HashMap<>();

        for (Map.Entry<String, Double> entry : crispInputs.entrySet()) {
            String varName = entry.getKey();
            LinguisticVariable variable = inputVariables.get(varName);

            if (variable == null) {
                continue;
            }

            double crispValue = variable.validateInput(entry.getValue());
            Map<String, Double> memberships = new HashMap<>();

            for (FuzzySet fuzzySet : variable.getAllFuzzySets()) {
                double membership = fuzzySet.getMembership(crispValue);
                if (membership > 0) {
                    memberships.put(fuzzySet.getName(), membership);
                }
            }

            fuzzified.put(varName, memberships);
        }

        return fuzzified;
    }

    /**
     * Defuzzification step: converts fuzzy output to crisp value.
     * 
     * @param inferredOutput map of output fuzzy set names to membership degrees
     *                       (Mamdani)
     *                       or crisp values (Sugeno)
     * @return crisp output value
     */
    private double defuzzify(Map<String, Double> inferredOutput) {
        if (inferenceEngine instanceof SugenoInference) {
            return inferredOutput.getOrDefault("output", 0.0);
        }

        return defuzzificationMethod.defuzzify(
                x -> {
                    double maxMu = 0.0;
                    for (Map.Entry<String, Double> entry : inferredOutput.entrySet()) {
                        FuzzySet fuzzySet = outputVariable.getFuzzySet(entry.getKey());
                        if (fuzzySet != null) {
                            double mu = fuzzySet.getMembership(x);
                            if (inferenceEngine instanceof MamdaniInference) {
                                MamdaniInference mamdani = (MamdaniInference) inferenceEngine;
                                mu = mamdani.getImplicationOperator().apply(entry.getValue(), mu);
                            }
                            maxMu = Math.max(maxMu, mu);
                        }
                    }
                    return maxMu;
                },
                outputVariable.getMinDomain(),
                outputVariable.getMaxDomain());
    }

    /**
     * Gets intermediate fuzzification results for debugging/visualization.
     */
    public Map<String, Map<String, Double>> getFuzzificationResults(Map<String, Double> crispInputs) {
        return fuzzify(crispInputs);
    }

    /**
     * Gets intermediate inference results for debugging/visualization.
     */
    public Map<String, Double> getInferenceResults(Map<String, Double> crispInputs) {
        Map<String, Map<String, Double>> fuzzifiedInputs = fuzzify(crispInputs);
        return inferenceEngine.infer(ruleBase.getEnabledRules(), fuzzifiedInputs, outputVariable);
    }
}
