package fuzzy.membership;

/**
 * Gaussian membership function defined by center (mean) and width (standard
 * deviation).
 * The function reaches its maximum (1.0) at the center.
 */
public class GaussianMF implements MembershipFunction {
    private final double center; // mean
    private final double width; // standard deviation

    public GaussianMF(double center, double width) {
        if (width <= 0) {
            throw new IllegalArgumentException("Gaussian MF width must be positive");
        }
        this.center = center;
        this.width = width;
    }

    @Override
    public double calculate(double x) {
        return Math.exp(-0.5 * Math.pow((x - center) / width, 2));
    }

    @Override
    public double[] getDomain() {
        // Gaussian is theoretically infinite, but we use 3-sigma rule for practical
        // domain
        return new double[] { center - 3 * width, center + 3 * width };
    }

    public double getCenter() {
        return center;
    }

    public double getWidth() {
        return width;
    }
}
