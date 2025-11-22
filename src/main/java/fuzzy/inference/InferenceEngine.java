package fuzzy.inference;

import fuzzy.rules.Rule;
import fuzzy.variables.LinguisticVariable;
import java.util.Map;

/**
 * Base interface for inference engines (Mamdani, Sugeno).
 */
public interface InferenceEngine {
    /**
     * Performs inference on enabled rules given fuzzified inputs.
     * 
     * @param rules           list of rules to evaluate
     * @param fuzzifiedInputs map of variable name to fuzzy set name to membership
     *                        degree
     * @param outputVariable  the output linguistic variable
     * @return map of output fuzzy set names to their membership degrees (for
     *         Mamdani)
     *         or crisp values (for Sugeno)
     */
    Map<String, Double> infer(java.util.List<Rule> rules,
            Map<String, Map<String, Double>> fuzzifiedInputs,
            LinguisticVariable outputVariable);
}
