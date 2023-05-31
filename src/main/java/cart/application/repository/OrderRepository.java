package cart.application.repository;

import cart.domain.Member;
import cart.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository {

    void insert(Order order);

    List<Order> findByMemberId(Long id);

    Order findById(Member member);
}
