package cart.domain.order;

import java.util.List;

public interface OrderItemRepository {

    List<OrderItem> findOrderItemsByOrderId(Long orderId);

    void createOrderItem(Long orderId, OrderItem orderItem);
}
