package cart.coupon.domain;

public class AllProductsCouponStrategy implements CouponStrategy {

    @Override
    public boolean canApply(Long productId) {
        return true;
    }

    @Override
    public TargetType getTargetType() {
        return TargetType.ALL;
    }
}
