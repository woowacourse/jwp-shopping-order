package cart.domain.point;

public class PointPolicy implements PointPolicyStrategy{
    private static final double POINT_POLICY = 0.1;

    @Override
    public Long caclulatePointWithPolicy(Long amountPayment) {
        double caculatedPoint = amountPayment * POINT_POLICY;
        return (long) caculatedPoint;
    }

    @Override
    public int getPointPercentage() {
        double pointPercentage = POINT_POLICY * 100;
        return (int) pointPercentage;
    }
}
