package cart.domain.repository;

import cart.domain.Member;
import cart.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository {
    Long saveOrder(Order order);

    List<Order> findAllByMemberId(Member member);
}
