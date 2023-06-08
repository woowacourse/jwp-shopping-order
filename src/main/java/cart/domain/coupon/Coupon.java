package cart.domain.coupon;

public interface Coupon {

    boolean isAvailable(final Integer totalPrice);

    Integer calculateDiscount(final Integer totalPrice);

    CouponInfo getCouponInfo();

    Integer getDiscountAmount();

    Double getDiscountPercentage();
}
