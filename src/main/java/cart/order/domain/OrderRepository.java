package cart.order.domain;

import java.util.List;

public interface OrderRepository {

    Long save(Order order);

    Order findById(Long id);

    List<Order> findAllByMemberId(Long memberId);
}
