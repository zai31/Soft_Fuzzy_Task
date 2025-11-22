package fuzzy.variables;

import fuzzy.membership.MembershipFunction;

/**
 * Represents a fuzzy set with a name and a membership function.
 */
public class FuzzySet {
    private final String name;
    private final MembershipFunction membershipFunction;

    public FuzzySet(String name, MembershipFunction membershipFunction) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Fuzzy set name cannot be null or empty");
        }
        if (membershipFunction == null) {
            throw new IllegalArgumentException("Membership function cannot be null");
        }
        this.name = name;
        this.membershipFunction = membershipFunction;
    }

    /**
     * Calculates the membership degree of a crisp value.
     */
    public double getMembership(double x) {
        return membershipFunction.calculate(x);
    }

    public String getName() {
        return name;
    }

    public MembershipFunction getMembershipFunction() {
        return membershipFunction;
    }

    @Override
    public String toString() {
        return name;
    }
}
