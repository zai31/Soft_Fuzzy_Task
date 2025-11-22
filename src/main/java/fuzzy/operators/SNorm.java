package fuzzy.operators;

/**
 * Interface for s-norms (OR operators) in fuzzy logic.
 */
public interface SNorm {
    /**
     * Computes the s-norm (OR) of two membership degrees.
     * 
     * @param a first membership degree
     * @param b second membership degree
     * @return result of s-norm operation
     */
    double compute(double a, double b);
}
