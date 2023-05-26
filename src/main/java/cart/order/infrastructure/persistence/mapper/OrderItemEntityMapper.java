package cart.order.infrastructure.persistence.mapper;

import cart.order.domain.OrderItem;
import cart.order.infrastructure.persistence.entity.OrderItemEntity;
import java.util.List;
import java.util.stream.Collectors;

public class OrderItemEntityMapper {

    public static List<OrderItemEntity> toEntities(List<OrderItem> orderItems, Long orderId) {
        return orderItems.stream()
                .map(it -> toEntity(orderId, it))
                .collect(Collectors.toList());
    }

    private static OrderItemEntity toEntity(Long orderId, OrderItem orderItem) {
        return new OrderItemEntity(
                orderItem.getId(),
                orderItem.getQuantity(),
                orderItem.getProductId(),
                orderItem.getName(),
                orderItem.getProductPrice(),
                orderItem.getImageUrl(),
                orderId);
    }

    public static List<OrderItem> toDomains(List<OrderItemEntity> orderItemEntities) {
        return orderItemEntities.stream()
                .map(it -> new OrderItem(
                        it.getId(),
                        it.getQuantity(),
                        it.getProductId(),
                        it.getName(),
                        it.getProductPrice(), it.getImageUrl())
                ).collect(Collectors.toList());
    }
}
