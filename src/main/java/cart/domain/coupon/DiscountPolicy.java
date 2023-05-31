package cart.domain.coupon;

import cart.domain.common.Money;

public interface DiscountPolicy {

    Money calculatePrice(final Long discountValue, final Money price);

    Money calculateDeliveryFee(final Long discountValue, final Money deliveryFee);
}
