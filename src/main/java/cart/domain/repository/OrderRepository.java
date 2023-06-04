package cart.domain.repository;

import cart.domain.Member;
import cart.domain.Order;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(Long id);

    List<Order> findAllByMember(Member member);

}
