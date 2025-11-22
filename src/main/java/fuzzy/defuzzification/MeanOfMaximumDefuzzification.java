package fuzzy.defuzzification;

import java.util.function.Function;
import java.util.ArrayList;
import java.util.List;

/**
 * Mean of Maximum (MOM) defuzzification method.
 * Finds all points with maximum membership and returns their average.
 */
public class MeanOfMaximumDefuzzification implements DefuzzificationMethod {
    private static final int DEFAULT_SAMPLES = 1000;
    private final int samples;

    public MeanOfMaximumDefuzzification() {
        this(DEFAULT_SAMPLES);
    }

    public MeanOfMaximumDefuzzification(int samples) {
        if (samples <= 0) {
            throw new IllegalArgumentException("Number of samples must be positive");
        }
        this.samples = samples;
    }

    @Override
    public double defuzzify(Function<Double, Double> aggregatedMF,
            double minDomain, double maxDomain) {
        double step = (maxDomain - minDomain) / samples;
        double maxMu = 0.0;
        List<Double> maxPoints = new ArrayList<>();

        for (int i = 0; i <= samples; i++) {
            double x = minDomain + i * step;
            double mu = aggregatedMF.apply(x);

            if (mu > maxMu) {
                maxMu = mu;
                maxPoints.clear();
                maxPoints.add(x);
            } else if (mu == maxMu && maxMu > 0) {
                maxPoints.add(x);
            }
        }

        if (maxPoints.isEmpty()) {
            return (minDomain + maxDomain) / 2.0;
        }

        double sum = 0.0;
        for (Double point : maxPoints) {
            sum += point;
        }
        return sum / maxPoints.size();
    }
}
