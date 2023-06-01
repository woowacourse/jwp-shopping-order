package cart.persistence.mapper;

import cart.domain.cartitem.CartItemWithId;
import cart.domain.member.dto.MemberWithId;
import cart.domain.order.Order;
import cart.domain.product.dto.ProductWithId;
import cart.persistence.entity.OrderEntity;
import cart.persistence.entity.OrderProductEntity;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderEntity convertOrderEntity(final Order order, final MemberWithId member) {
        return new OrderEntity(member.getMemberId(), order.getTotalPrice(), order.getDiscountedTotalPrice(),
            order.getDeliveryPrice(), order.getOrderedAt());
    }

    public static List<OrderProductEntity> convertOrderProductEntities(final List<CartItemWithId> cartItems,
                                                                       final Long orderId) {
        return cartItems.stream()
            .map(cartItemWithId -> convertOrderProductEntity(orderId, cartItemWithId))
            .collect(Collectors.toUnmodifiableList());
    }

    private static OrderProductEntity convertOrderProductEntity(final Long orderId,
                                                                final CartItemWithId cartItemWithId) {
        final ProductWithId productWithId = cartItemWithId.getProduct();
        return new OrderProductEntity(orderId, productWithId.getProductId(),
            productWithId.getProduct().getPrice(), cartItemWithId.getQuantity());
    }
}
