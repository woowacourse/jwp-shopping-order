package cart.domain.coupon;

import cart.domain.common.Money;

public interface DiscountPolicy {

    Money calculatePrice(final Money price);

    Money calculateDeliveryFee(final Money deliveryFee);

    DiscountPolicyType getDiscountPolicyType();

    Money getDiscountPrice();

    int getDiscountPercent();

    boolean isDiscountDeliveryFee();
}
