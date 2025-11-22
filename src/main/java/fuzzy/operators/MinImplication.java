package fuzzy.operators;

/**
 * Minimum implication: min(antecedent_strength, consequent_MF)
 */
public class MinImplication implements ImplicationOperator {
    @Override
    public double apply(double antecedentStrength, double consequentMembership) {
        return Math.min(antecedentStrength, consequentMembership);
    }
}
