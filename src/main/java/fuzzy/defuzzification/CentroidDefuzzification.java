package fuzzy.defuzzification;

import java.util.function.Function;

/**
 * Centroid (Center of Gravity) defuzzification method.
 * Computes the weighted average of the aggregated membership function.
 */
public class CentroidDefuzzification implements DefuzzificationMethod {
    private static final int DEFAULT_SAMPLES = 1000;
    private final int samples;

    public CentroidDefuzzification() {
        this(DEFAULT_SAMPLES);
    }

    public CentroidDefuzzification(int samples) {
        if (samples <= 0) {
            throw new IllegalArgumentException("Number of samples must be positive");
        }
        this.samples = samples;
    }

    @Override
    public double defuzzify(Function<Double, Double> aggregatedMF,
            double minDomain, double maxDomain) {
        double step = (maxDomain - minDomain) / samples;
        double numerator = 0.0;
        double denominator = 0.0;

        for (int i = 0; i <= samples; i++) {
            double x = minDomain + i * step;
            double mu = aggregatedMF.apply(x);
            numerator += x * mu;
            denominator += mu;
        }

        if (denominator == 0.0) {
            return (minDomain + maxDomain) / 2.0;
        }

        return numerator / denominator;
    }
}
