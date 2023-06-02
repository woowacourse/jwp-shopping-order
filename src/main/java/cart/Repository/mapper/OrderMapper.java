package cart.Repository.mapper;

import cart.domain.CartItem;
import cart.domain.Order.Order;
import cart.domain.Order.OrderItem;
import cart.domain.Product.Product;
import cart.entity.MemberEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.entity.ProductEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cart.Repository.mapper.MemberMapper.toMember;
import static cart.Repository.mapper.ProductMapper.productMappingById;

public class OrderMapper {

    public static List<Order> toOrders(List<OrderEntity> orders, Map<Long, List<OrderItemEntity>> orderItemsByOrderId, List<ProductEntity> productEntities, MemberEntity memberEntity) {
        return orders.stream()
                .map(orderEntity -> toOrder(orderEntity, orderItemsByOrderId.get(orderEntity.getId()), productEntities, memberEntity))
                .collect(Collectors.toUnmodifiableList());
    }

    public static OrderItem toOrderItem(OrderItemEntity orderItemEntity, Map<Long, Product> productMappingById) {
        Long productId = orderItemEntity.getProductId();

        return new OrderItem(
                productMappingById.get(productId),
                orderItemEntity.getQuantity()
        );
    }

    public static Order toOrder(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities, List<ProductEntity> productEntities, MemberEntity memberEntity) {
        Map<Long, Product> productMappingById = productMappingById(productEntities);

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

    public static List<OrderItem> toOrderItemsFrom(List<CartItem> cartItems){
        return cartItems.stream().map(it ->
                new OrderItem(
                        it.getProduct(),
                        it.getQuantity()
                )
        ).collect(Collectors.toUnmodifiableList());
    }

}
