package cart.step2.order.domain.repository;

import cart.step2.order.domain.OrderItemEntity;

import java.util.List;

public interface OrderItemRepository {
    void createAllOrderItems(final List<OrderItemEntity> orderItemEntities);

}
