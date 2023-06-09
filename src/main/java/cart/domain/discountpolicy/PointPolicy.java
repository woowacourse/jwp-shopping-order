package cart.domain.discountpolicy;

public enum PointPolicy {

    EARNED_RATE(0.01);

    private final double value;

    PointPolicy(double value) {
        this.value = value;
    }

    public int calculateEarnedPoint(int totalPrice) {
        return (int) (totalPrice * value);
    }

}

