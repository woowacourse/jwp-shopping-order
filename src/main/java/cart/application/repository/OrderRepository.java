package cart.application.repository;

import cart.application.domain.Member;
import cart.application.domain.Order;
import java.util.List;

public interface OrderRepository {

    Order save(final Order order);

    Order findById(final Long id, final Member member);

    List<Order> findMemberOrders(final Member member);
}
