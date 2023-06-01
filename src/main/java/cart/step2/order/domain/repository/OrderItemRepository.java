package cart.step2.order.domain.repository;

import cart.step2.order.domain.OrderItem;

import java.util.List;

public interface OrderItemRepository {
    void createAllOrderItems(final List<OrderItem> orderItems);

}
