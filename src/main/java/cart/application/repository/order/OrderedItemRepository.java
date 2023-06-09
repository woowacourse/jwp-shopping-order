package cart.application.repository.order;

import cart.domain.order.OrderItems;

public interface OrderedItemRepository {

    void createOrderItems(final Long orderId, final OrderItems orderItems);

    OrderItems findOrderItemsByOrderId(final Long orderId);

}
