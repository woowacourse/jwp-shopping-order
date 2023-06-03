package cart.persistence.mapper;

import static cart.persistence.mapper.CartMapper.convertCartItems;
import static cart.persistence.mapper.CouponMapper.convertCouponWithId;
import static cart.persistence.mapper.MemberMapper.convertMemberWithId;

import cart.domain.cartitem.CartItemWithId;
import cart.domain.coupon.CouponWithId;
import cart.domain.member.MemberWithId;
import cart.domain.order.BasicOrder;
import cart.domain.order.CouponOrder;
import cart.domain.order.Order;
import cart.domain.order.OrderWithId;
import cart.domain.product.ProductWithId;
import cart.persistence.dao.dto.OrderDto;
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

    public static OrderWithId convertOrderWithId(final List<OrderDto> orderDto) {
        final OrderDto order = orderDto.get(0);
        final MemberWithId member = convertMemberWithId(order);
        final List<CartItemWithId> cartItems = convertCartItems(orderDto);

        if (order.getCouponId() == 0) {
            final Order basicOrder = new BasicOrder(member, order.getDeliveryPrice(), order.getOrderedAt(), cartItems,
                order.getIsValid());
            return new OrderWithId(order.getOrderId(), basicOrder);
        }
        final CouponWithId coupon = convertCouponWithId(order);
        final Order couponOrder = new CouponOrder(member, coupon, order.getDeliveryPrice(),
            order.getOrderedAt(), cartItems, order.getIsValid());
        return new OrderWithId(order.getOrderId(), couponOrder);
    }

    private static OrderProductEntity convertOrderProductEntity(final Long orderId,
                                                                final CartItemWithId cartItemWithId) {
        final ProductWithId productWithId = cartItemWithId.getProduct();
        return new OrderProductEntity(orderId, productWithId.getProductId(),
            productWithId.getProduct().getPrice(), cartItemWithId.getQuantity());
    }
}
