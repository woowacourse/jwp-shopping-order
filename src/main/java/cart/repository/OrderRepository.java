package cart.repository;

import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.Order;

public interface OrderRepository {
    Long createOrder(final Order order, final CartItems cartItems);

    CartItems findCartItemsByMemberId(Long memberId);

    Order findOrder(Long orderId, Member member);
}
