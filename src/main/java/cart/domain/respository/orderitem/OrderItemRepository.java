package cart.domain.respository.orderitem;

import cart.domain.order.OrderItem;

public interface OrderItemRepository {

    OrderItem insert(final Long orderId, final OrderItem orderItem);
}
