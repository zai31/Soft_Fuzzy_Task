package fuzzy.operators;

/**
 * Maximum aggregation: takes the maximum of all values.
 */
public class MaxAggregation implements AggregationOperator {
    @Override
    public double aggregate(double... values) {
        if (values == null || values.length == 0) {
            return 0.0;
        }
        double max = values[0];
        for (int i = 1; i < values.length; i++) {
            max = Math.max(max, values[i]);
        }
        return max;
    }
}
