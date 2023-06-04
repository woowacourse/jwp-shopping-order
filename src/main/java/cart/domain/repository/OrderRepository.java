package cart.domain.repository;

import cart.domain.Order;
import java.util.List;

public interface OrderRepository {

    Order create(final Order order, final Long memberId);

    Order findById(final Long id, final Long memberId);

    List<Order> findAll(final Long memberId);
}
