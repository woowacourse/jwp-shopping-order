package cart.domain.repository;

import cart.domain.Member;
import cart.domain.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
public interface OrderRepository {
    Long saveOrder(Order order);

    List<Order> findAllByMemberId(Member member);

    Order findByOrderId(Member member, Long orderId);

    void deleteOrder(Long orderId);

    void confirmOrder(Long orderId, Member member);

    boolean checkConfirmState(Long orderId);
}
