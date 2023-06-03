package cart.domain.respository.order;

import cart.domain.order.Order;
import cart.domain.order.OrderPrice;
import java.util.List;

public interface OrderRepository {

    Order insert(final Order order, final OrderPrice orderPrice);

    Order findByOrderId(final Long orderId);

    List<Order> findAllByMemberId(final Long memberId);
}
