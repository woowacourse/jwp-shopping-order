package cart.coupon.domain;

public class GeneralCouponType implements CouponType{

    @Override
    public boolean canApply(Long productId) {
        return true;
    }

    @Override
    public TargetType getTargetType() {
        return TargetType.ALL;
    }
}
