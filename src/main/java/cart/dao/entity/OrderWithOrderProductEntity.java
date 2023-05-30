package cart.dao.entity;

public class OrderWithOrderProductEntity {
    private final OrderEntity orderEntity;
    private final OrderProductEntity orderProductEntity;

    public OrderWithOrderProductEntity(final OrderEntity orderEntity, final OrderProductEntity orderProductEntity) {
        this.orderEntity = orderEntity;
        this.orderProductEntity = orderProductEntity;
    }

    public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    public OrderProductEntity getOrderProductEntity() {
        return orderProductEntity;
    }
}
