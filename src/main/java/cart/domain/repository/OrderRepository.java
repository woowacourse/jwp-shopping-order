package cart.domain.repository;

import cart.domain.member.Member;
import cart.domain.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    long createOrder(Order order);

    Optional<List<Order>> findOrderProductsByMemberId(Member member);

    Optional<Order> findOrderById(Member member, long orderId);
}
