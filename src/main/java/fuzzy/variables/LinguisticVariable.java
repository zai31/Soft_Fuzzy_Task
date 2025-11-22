package fuzzy.variables;

import java.util.*;

/**
 * Represents a linguistic variable with a name, domain, and multiple fuzzy
 * sets.
 */
public class LinguisticVariable {
    private final String name;
    private final double minDomain;
    private final double maxDomain;
    private final Map<String, FuzzySet> fuzzySets;

    public LinguisticVariable(String name, double minDomain, double maxDomain) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Variable name cannot be null or empty");
        }
        if (minDomain >= maxDomain) {
            throw new IllegalArgumentException("minDomain must be less than maxDomain");
        }
        this.name = name;
        this.minDomain = minDomain;
        this.maxDomain = maxDomain;
        this.fuzzySets = new LinkedHashMap<>();
    }

    /**
     * Adds a fuzzy set to this variable.
     */
    public void addFuzzySet(FuzzySet fuzzySet) {
        if (fuzzySet == null) {
            throw new IllegalArgumentException("Fuzzy set cannot be null");
        }
        fuzzySets.put(fuzzySet.getName(), fuzzySet);
    }

    /**
     * Gets a fuzzy set by name.
     */
    public FuzzySet getFuzzySet(String name) {
        return fuzzySets.get(name);
    }

    /**
     * Gets all fuzzy sets.
     */
    public Collection<FuzzySet> getAllFuzzySets() {
        return fuzzySets.values();
    }

    /**
     * Validates and clamps an input value to the domain.
     */
    public double validateInput(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return (minDomain + maxDomain) / 2.0; // default to center
        }
        return Math.max(minDomain, Math.min(maxDomain, value));
    }

    public String getName() {
        return name;
    }

    public double getMinDomain() {
        return minDomain;
    }

    public double getMaxDomain() {
        return maxDomain;
    }

    public int getFuzzySetCount() {
        return fuzzySets.size();
    }
}
