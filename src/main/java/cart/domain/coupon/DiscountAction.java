package cart.domain.coupon;

import cart.domain.Order;

public interface DiscountAction {
    int discount(Order order);
}
