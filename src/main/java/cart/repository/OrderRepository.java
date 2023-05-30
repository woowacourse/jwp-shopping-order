package cart.repository;

import cart.domain.Member;
import cart.domain.Order;
import java.util.List;

public interface OrderRepository {

    Order save(final Order order);

    Order findById(final Long id, final Member member);

    List<Order> findMemberOrders(final Member member);
}
