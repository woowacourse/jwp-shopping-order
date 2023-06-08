package cart.Repository.mapper;

import cart.domain.Cart;
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
import static cart.Repository.mapper.ProductMapper.toProduct;

public class OrderMapper {

    public static List<Order> toOrders(List<OrderEntity> orders, Map<Long, List<OrderItemEntity>> orderItemsByOrderId, Map<Long, ProductEntity> productEntities, MemberEntity memberEntity) {
        return orders.stream()
                .map(orderEntity ->
                        toOrder(
                                orderEntity,
                                orderItemsByOrderId.get(orderEntity.getId()),
                                productEntities,
                                memberEntity))
                .collect(Collectors.toUnmodifiableList());
    }

    public static OrderItem toOrderItem(OrderItemEntity orderItemEntity, ProductEntity productEntity) {

        return new OrderItem(
                toProduct(productEntity),
                orderItemEntity.getQuantity()
        );
    }

    public static Order toOrder(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities, Map<Long, ProductEntity> productEntityByIds, MemberEntity memberEntity) {
        List<OrderItem> orderItems = orderItemEntities.stream()
                .map(it -> toOrderItem(it, productEntityByIds.get(it.getProductId())))
                .collect(Collectors.toUnmodifiableList());

        return new Order(
                orderEntity.getId(),
                orderEntity.getOrderDate(),
                toMember(memberEntity),
                orderItems
        );
    }

    public static List<OrderItem> toOrderItemsFrom(Cart cart){

        return cart.getCartItems()
                .stream().map(cartItem ->
                new OrderItem(
                        cartItem.getProduct(),
                        cartItem.getQuantity()
                )
        ).collect(Collectors.toUnmodifiableList());
    }

}
