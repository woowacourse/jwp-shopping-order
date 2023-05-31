package cart.domain2.coupon;

import cart.domain2.common.Money;

public class NoneDiscountPolicy implements DiscountPolicy {

    @Override
    public Money calculatePrice(final Long discountValue, final Money price) {
        return price;
    }

    @Override
    public Money calculateDeliveryFee(final Long discountValue, final Money deliveryFee) {
        return deliveryFee;
    }
}
