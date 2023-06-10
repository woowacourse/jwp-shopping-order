package cart.repository;

import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.Order;
import java.util.List;

public interface OrderRepository {
    Long createOrder(final Order order, final CartItems cartItems);

    CartItems findCartItemsByMemberId(final Member member);

    Order findOrder(final Long orderId, final Member member);

    List<Order> findAllByMember(Member member);
}
