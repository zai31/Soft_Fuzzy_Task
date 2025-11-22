package fuzzy.operators;

/**
 * Interface for aggregation operators (combining multiple rule outputs).
 */
public interface AggregationOperator {
    /**
     * Aggregates multiple membership degrees.
     * 
     * @param values array of membership degrees
     * @return aggregated value
     */
    double aggregate(double... values);
}
