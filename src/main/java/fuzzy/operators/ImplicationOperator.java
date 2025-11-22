package fuzzy.operators;

/**
 * Interface for implication operators (applying rule strength to consequent).
 */
public interface ImplicationOperator {
    /**
     * Applies implication: consequent = implication(antecedent_strength,
     * consequent_MF).
     * 
     * @param antecedentStrength   the firing strength of the rule
     * @param consequentMembership the original membership function value
     * @return the modified membership value
     */
    double apply(double antecedentStrength, double consequentMembership);
}
