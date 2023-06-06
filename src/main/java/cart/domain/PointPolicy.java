package cart.domain;

public class PointPolicy {

    private final static double defaultAccumulationRate = 0.1;

    private final double accumulationRate;

    public PointPolicy() {
        this.accumulationRate = defaultAccumulationRate;
    }

    public PointPolicy(final double accumulationRate) {
        this.accumulationRate = accumulationRate;
    }

    public PointPolicy(final long pointPercentage) {
        this.accumulationRate = pointPercentage * 0.01;
    }

    public long getPointPercentage() {
        return (long) (accumulationRate * 100);
    }

    public Point getEarnedPoint(final long price) {
        return new Point((long) (price * accumulationRate));
    }
}
