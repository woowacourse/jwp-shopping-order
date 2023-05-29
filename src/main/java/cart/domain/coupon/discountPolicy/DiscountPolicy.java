package cart.domain.coupon.discountPolicy;

import cart.domain.OrderPrice;

public interface DiscountPolicy {

    OrderPrice discount(final OrderPrice orderPrice);

    String getName();

    long getDiscountPrice();

    int getDiscountPercent();

    boolean isDiscountDeliveryFee();

}
