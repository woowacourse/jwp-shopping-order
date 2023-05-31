package cart.application.repository;

import cart.domain.Member;
import cart.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository {

    Order insert(Order order);

    List<Order> findByMemberId(Long id);

    Optional<Order> findById(Long id);
}
