package fuzzy.operators;

/**
 * Product implication: antecedent_strength * consequent_MF
 */
public class ProductImplication implements ImplicationOperator {
    @Override
    public double apply(double antecedentStrength, double consequentMembership) {
        return antecedentStrength * consequentMembership;
    }
}
