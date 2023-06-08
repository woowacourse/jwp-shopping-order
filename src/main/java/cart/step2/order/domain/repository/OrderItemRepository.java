package cart.step2.order.domain.repository;

import cart.step2.order.domain.OrderItemEntity;

import java.util.List;

public interface OrderItemRepository {
    void batchInsert(final List<OrderItemEntity> orderItemEntities);

    List<OrderItemEntity> findByOrderId(final Long orderId);

}
