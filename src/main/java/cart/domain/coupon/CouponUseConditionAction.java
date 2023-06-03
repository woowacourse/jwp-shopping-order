package cart.domain.coupon;

import cart.domain.order.Order;

public interface CouponUseConditionAction {
    boolean isUsable(Order order);
}
