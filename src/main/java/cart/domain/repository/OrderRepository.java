package cart.domain.repository;

import cart.domain.member.Member;
import cart.domain.order.Order;

import java.util.List;

public interface OrderRepository {

    long createOrder(Order order);

    List<Order> findOrderProductsByMemberId(Member member);

    Order findOrderById(Member member, long orderId);
}
