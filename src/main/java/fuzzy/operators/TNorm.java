package fuzzy.operators;

/**
 * Interface for t-norms (AND operators) in fuzzy logic.
 */
public interface TNorm {
    /**
     * Computes the t-norm (AND) of two membership degrees.
     * 
     * @param a first membership degree
     * @param b second membership degree
     * @return result of t-norm operation
     */
    double compute(double a, double b);
}
