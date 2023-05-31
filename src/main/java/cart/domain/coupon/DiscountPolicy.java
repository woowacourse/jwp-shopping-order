package cart.domain.coupon;

import cart.domain.VO.Money;

public interface DiscountPolicy {

    Money calculatePrice(final Long discountValue, final Money price);

    Money calculateDeliveryFee(final Long discountValue, final Money deliveryFee);
}
