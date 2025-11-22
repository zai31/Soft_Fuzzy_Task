package fuzzy.operators;

/**
 * Maximum s-norm: max(a, b)
 */
public class MaxSNorm implements SNorm {
    @Override
    public double compute(double a, double b) {
        return Math.max(a, b);
    }
}
