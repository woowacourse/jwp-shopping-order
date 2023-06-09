package cart.domain.order;

import java.util.List;

public interface OrderRepository {

    Long save(final Order order);

    List<Order> findOrderByMemberId(final Long id);
}
