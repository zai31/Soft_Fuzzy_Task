package fuzzy.defuzzification;

/**
 * Interface for defuzzification methods.
 */
public interface DefuzzificationMethod {
    /**
     * Defuzzifies an aggregated output fuzzy set to a crisp value.
     * 
     * @param aggregatedMF a function that returns membership degree for a given x
     * @param minDomain    minimum domain value
     * @param maxDomain    maximum domain value
     * @return crisp output value
     */
    double defuzzify(java.util.function.Function<Double, Double> aggregatedMF,
            double minDomain, double maxDomain);
}
