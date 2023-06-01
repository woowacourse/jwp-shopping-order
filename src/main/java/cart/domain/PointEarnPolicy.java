package cart.domain;

public enum PointEarnPolicy {

    DEFAULT(0.1d);

    private final double percent;

    PointEarnPolicy(double percent) {
        this.percent = percent;
    }

    public Money calculateEarnPoints(Money totalPrice) {
        return totalPrice.multiply(percent);
    }
}
