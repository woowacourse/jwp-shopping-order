package cart.coupon.domain;

public class GeneralCouponStrategy implements CouponStrategy {

    @Override
    public boolean canApply(Long productId) {
        return true;
    }

    @Override
    public TargetType getTargetType() {
        return TargetType.ALL;
    }
}
