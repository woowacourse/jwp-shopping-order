package cart.coupon.domain;

public interface CouponType {

    boolean canApply(Long productId);

    TargetType getTargetType();
}
