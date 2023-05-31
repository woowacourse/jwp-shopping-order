package cart.domain2.coupon;

import cart.domain2.common.Money;

public interface DiscountPolicy {

    Money calculatePrice(final Long discountValue, final Money price);

    Money calculateDeliveryFee(final Long discountValue, final Money deliveryFee);
}
