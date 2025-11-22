package fuzzy.membership;

/**
 * Trapezoidal membership function defined by four points: a (left base), b
 * (left top),
 * c (right top), d (right base). The function is 1.0 in the interval [b, c].
 */
public class TrapezoidalMF implements MembershipFunction {
    private final double a; // left base point
    private final double b; // left top point
    private final double c; // right top point
    private final double d; // right base point

    public TrapezoidalMF(double a, double b, double c, double d) {
        if (a >= b || b >= c || c >= d) {
            throw new IllegalArgumentException("Trapezoidal MF requires a < b < c < d");
        }
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public double calculate(double x) {
        if (x <= a || x >= d) {
            return 0.0;
        }
        if (x >= b && x <= c) {
            return 1.0;
        }
        if (x < b) {
            return (x - a) / (b - a);
        } else {
            return (d - x) / (d - c);
        }
    }

    @Override
    public double[] getDomain() {
        return new double[] { a, d };
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getD() {
        return d;
    }
}
