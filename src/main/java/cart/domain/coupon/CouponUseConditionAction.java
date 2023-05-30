package cart.domain.coupon;

import cart.domain.Order;

public interface CouponUseConditionAction {
    boolean isUsable(Order order);
}
