package cart.application.repository;

import cart.domain.Member;
import cart.domain.order.Order;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    long order(Order order);

    Optional<Order> findById(long id);

    List<Order> findByMember(Member member);
}
