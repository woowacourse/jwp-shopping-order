package cart.dao.entity;

import java.util.List;

public class OrderWithOrderProductEntities {
    private final OrderEntity orderEntity;
    private final List<OrderProductEntity> orderProductEntities;

    public OrderWithOrderProductEntities(final OrderEntity orderEntity,
                                         final List<OrderProductEntity> orderProductEntities) {
        this.orderEntity = orderEntity;
        this.orderProductEntities = orderProductEntities;
    }

    public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    public List<OrderProductEntity> getOrderProductEntities() {
        return orderProductEntities;
    }
}
