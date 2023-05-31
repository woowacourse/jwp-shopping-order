package cart.domain.repository;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Point;
import cart.domain.Product;

import java.util.List;

public interface OrderRepository {

    Product findProductById(Long id);

    Long saveOrder(Order order);

    void updateMemberPoint(Member member, Point newPoint);

    void deleteCartItemByMember(Member member);

    List<Order> findOrdersByMemberId(Member member);

    Order findOrderByOrderId(Long orderId, Member member);
}
