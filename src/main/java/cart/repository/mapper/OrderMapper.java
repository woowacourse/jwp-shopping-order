package cart.repository.mapper;

import cart.domain.cart.Quantity;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.entity.MemberEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.entity.ProductEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderMapper {

    public static Order toOrder(
            final OrderEntity orderEntity,
            final List<OrderItemEntity> orderItemEntities,
            final Map<Long, ProductEntity> productGroupByProductId,
            final MemberEntity memberEntity
    ) {
        return new Order(
                orderEntity.getId(),
                MemberMapper.toMember(memberEntity),
                orderEntity.getOrderTime(),
                OrderMapper.toOrderItems(orderItemEntities, productGroupByProductId)
        );
    }

    private static List<OrderItem> toOrderItems(
            final List<OrderItemEntity> orderItemEntities,
            final Map<Long, ProductEntity> productGroupByProductId
    ) {
        return orderItemEntities.stream()
                .map(it -> OrderMapper.toOrderItem(it, productGroupByProductId.get(it.getProductId())))
                .collect(Collectors.toUnmodifiableList());
    }

    private static OrderItem toOrderItem(
            final OrderItemEntity orderItemEntity,
            final ProductEntity productEntity
    ) {
        return new OrderItem(
                orderItemEntity.getId(),
                new Quantity(orderItemEntity.getQuantity()),
                ProductMapper.toProductWithPrice(productEntity, orderItemEntity.getPriceAtOrder())
        );
    }

    public static OrderEntity toOrderEntity(final Order order) {
        return new OrderEntity(
                order.getId(),
                order.getMember().getId(),
                order.getOrderTime()
        );
    }

    public static List<OrderItemEntity> toOrderItemEntities(final List<OrderItem> orderItems, final Long orderId) {
        return orderItems.stream()
                .map(it -> toOrderItemEntity(it, orderId))
                .collect(Collectors.toUnmodifiableList());
    }

    private static OrderItemEntity toOrderItemEntity(final OrderItem orderItem, final Long orderId) {
        return new OrderItemEntity(
                orderItem.getId(),
                orderId,
                orderItem.getProduct().getId(),
                orderItem.getProduct().getPrice().price(),
                orderItem.getQuantity().quantity()
        );
    }
}
