package cart.application.repository.order;

import cart.domain.order.OrderItem;

import java.util.List;

public interface OrderedItemRepository {

    void createOrderItems(final Long orderId, final List<OrderItem> orderItems);

    List<OrderItem> findOrderItemsByOrderId(final Long orderId);

}
