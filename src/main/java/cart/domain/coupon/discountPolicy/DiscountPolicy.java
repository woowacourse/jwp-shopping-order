package cart.domain.coupon.discountPolicy;

import cart.domain.TotalPrice;

public interface DiscountPolicy {

    TotalPrice discount(final TotalPrice orderPrice);

    String getName();

    long getDiscountPrice();

    int getDiscountPercent();

    boolean isDiscountDeliveryFee();

}
