package cart.order.application;

import cart.order.domain.OrderItem;
import java.util.List;

public interface OrderItemRepository {
    void saveAll(Long orderId, List<OrderItem> orderItems);
    List<OrderItem> findByOrderId(Long orderId);
}
