package cart.domain;

import cart.domain.order.Order;

import java.util.Optional;

public interface OrderBaseCouponGenerator {

    Optional<Coupon> generate(Order order);
}
