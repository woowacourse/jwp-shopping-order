package cart.domain.point;

public interface PointPolicyStrategy {
    Long caclulatePointWithPolicy(Long amountPayment);
    int getPointPercentage();
}
