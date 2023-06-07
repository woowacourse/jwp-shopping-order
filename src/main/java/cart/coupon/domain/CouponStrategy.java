package cart.coupon.domain;

public interface CouponStrategy {

    boolean canApply(Long productId);

    TargetType getTargetType();
}
