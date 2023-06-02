package cart.Repository.mapper;

import cart.domain.Order.Order;
import cart.domain.Order.OrderItem;
import cart.domain.Product.Product;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public static List<Order> toOrders(List<OrderEntity> orders, Map<Long, List<OrderItemEntity>> orderItemsByOrderId, List<ProductEntity> productEntities, MemberEntity memberEntity) {
        return orders.stream()
                .map(orderEntity -> toOrder(orderEntity, orderItemsByOrderId.get(orderEntity.getId()), productEntities, memberEntity))
                .collect(Collectors.toUnmodifiableList());
    }

    public OrderItem toOrderItem(OrderItemEntity orderItemEntity, Map<Long, Product> productMappingById) {
        Long productId = orderItemEntity.getProductId();

        return new OrderItem(
                productMappingById.get(productId),
                orderItemEntity.getQuantity()
        );
    }

    public Order toOrder(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities, List<ProductEntity> productEntities) {
        Map<Long, Product> productMappingById = productMapper.productMappingById(productEntities);

        List<OrderItem> orderItems = orderItemEntities.stream()
                .map(it -> toOrderItem(it, productMappingById))
                .collect(Collectors.toUnmodifiableList());

        return new Order(
                orderEntity.getId(),
                orderEntity.getOrderDate(),
                toMember(memberEntity),
                orderItems
        );
    }

}
