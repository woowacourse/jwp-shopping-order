package cart.domain;

import cart.domain.Point;

public enum PointEarningPolicy {
    BASIC(0.01);

    private final double EARNING_RATE;

    PointEarningPolicy(double EARNING_RATE) {
        this.EARNING_RATE = EARNING_RATE;
    }

    public static Point calculateSavingPoints(Long price) {
        return new Point((long) (price * BASIC.EARNING_RATE));
    }
}
