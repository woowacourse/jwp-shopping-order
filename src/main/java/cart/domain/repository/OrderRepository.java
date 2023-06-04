package cart.domain.repository;

import cart.domain.Order;

public interface OrderRepository {

    Order create(final Order order, final Long memberId);

    Order findById(Long id, Long memberId);
}
