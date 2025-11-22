package fuzzy.membership;

/**
 * Base interface for all membership functions.
 * Defines the core contract for calculating membership degree.
 */
public interface MembershipFunction {
    /**
     * Calculates the membership degree of a given crisp value.
     * 
     * @param x the crisp input value
     * @return membership degree in [0, 1]
     */
    double calculate(double x);

    /**
     * Gets the domain range where this membership function is defined.
     * 
     * @return array with [min, max] domain bounds
     */
    double[] getDomain();
}
