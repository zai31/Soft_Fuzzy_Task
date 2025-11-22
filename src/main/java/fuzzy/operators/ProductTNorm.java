package fuzzy.operators;

/**
 * Product t-norm: a * b
 */
public class ProductTNorm implements TNorm {
    @Override
    public double compute(double a, double b) {
        return a * b;
    }
}
