package cart.dao.order;

import cart.domain.order.OrderItem;

import java.util.List;

public interface OrderItemDao {

    List<OrderItem> findOrderItemsByOrderId(Long orderId);

    void createOrderItem(Long orderId, OrderItem orderItem);
}
