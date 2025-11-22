package fuzzy.operators;

/**
 * Bounded sum s-norm: min(1, a + b)
 */
public class SumSNorm implements SNorm {
    @Override
    public double compute(double a, double b) {
        return Math.min(1.0, a + b);
    }
}
