package cart.application.repository.order;

import cart.domain.order.OrderItem;

import java.util.List;

public interface OrderedItemRepository {

    void createOrderItems(List<OrderItem> orderItem);

    List<OrderItem> findOrderItemsByOrderId(Long orderId);

}
