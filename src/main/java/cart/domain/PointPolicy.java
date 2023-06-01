package cart.domain;

public class PointPolicy {

    private final static double accumulationRate = 0.1;

    public static long getPointPercentage() {
        return (long) (accumulationRate * 100);
    }

    public static long getEarnedPoint(final long price) {
        return (long) (price * accumulationRate);
    }
}
