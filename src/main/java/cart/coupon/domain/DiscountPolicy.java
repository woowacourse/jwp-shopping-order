package cart.coupon.domain;

import cart.cartitem.domain.CartItem;

public interface DiscountPolicy {

    int calculatePrice(int price);

    int getValue();
}
