package fuzzy.operators;

/**
 * Minimum t-norm: min(a, b)
 */
public class MinTNorm implements TNorm {
    @Override
    public double compute(double a, double b) {
        return Math.min(a, b);
    }
}
