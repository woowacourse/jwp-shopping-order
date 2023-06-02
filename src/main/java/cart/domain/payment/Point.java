package cart.domain.payment;

public enum Point {

    POINT_RATIO(0.1),
    ;

    private final double pointRatio;

    Point(double pointRatio) {
        this.pointRatio = pointRatio;
    }

    public static int calculateEarningPoint(int userPayment) {
        return (int) Math.round(userPayment * POINT_RATIO.getPointRatio());
    }

    public double getPointRatio() {
        return pointRatio;
    }
}
