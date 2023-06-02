package cart.repository;

import cart.domain.Order;

public interface OrderRepository {

    Order create(final Order order, final Long memberId);
}
