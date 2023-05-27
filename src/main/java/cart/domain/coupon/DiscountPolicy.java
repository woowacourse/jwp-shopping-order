package cart.domain.coupon;

public interface DiscountPolicy {

    DiscountPolicyType getDiscountPolicyType();

    long getDiscountPrice();

    long getDiscountPercent();

    boolean isDiscountDeliveryFee();
}
