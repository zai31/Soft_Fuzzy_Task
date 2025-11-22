package fuzzy.membership;

/**
 * Triangular membership function defined by three points: a (left), b (peak), c
 * (right).
 * The function reaches its maximum (1.0) at point b.
 */
public class TriangularMF implements MembershipFunction {
    private final double a; // left base point
    private final double b; // peak point
    private final double c; // right base point

    public TriangularMF(double a, double b, double c) {
        if (a >= b || b >= c) {
            throw new IllegalArgumentException("Triangular MF requires a < b < c");
        }
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double calculate(double x) {
        if (x <= a || x >= c) {
            return 0.0;
        }
        if (x == b) {
            return 1.0;
        }
        if (x < b) {
            return (x - a) / (b - a);
        } else {
            return (c - x) / (c - b);
        }
    }

    @Override
    public double[] getDomain() {
        return new double[] { a, c };
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
}
