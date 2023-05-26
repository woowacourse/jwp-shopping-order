package cart.order.infrastructure.persistence.mapper;

import cart.order.domain.Order;
import cart.order.infrastructure.persistence.entity.OrderEntity;
import cart.order.infrastructure.persistence.entity.OrderItemEntity;
import java.util.List;

public class OrderEntityMapper {

    public static OrderEntity toEntity(Order order) {
        return new OrderEntity(order.getId(), order.getMemberId());
    }

    public static Order toDomain(
            OrderEntity orderEntity,
            List<OrderItemEntity> orderItemEntities
    ) {
        return new Order(
                orderEntity.getId(),
                orderEntity.getMemberId(),
                OrderItemEntityMapper.toDomains(orderItemEntities));
    }
}
