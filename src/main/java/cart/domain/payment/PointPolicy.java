package cart.domain.payment;

public enum PointPolicy {

    EARNING_POINT_RATIO(0.1),
    ;

    private final double pointRatio;

    PointPolicy(double pointRatio) {
        this.pointRatio = pointRatio;
    }

    public int calculateEarningPoint(int userPayment) {
        return (int) Math.round(userPayment * this.pointRatio);
    }
}
