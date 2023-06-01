package cart.repository;

import cart.domain.Member;
import cart.domain.Order;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Long save(Order order);

    List<Order> findByMember(Member member);

    Optional<Order> findById(Long id);

}
