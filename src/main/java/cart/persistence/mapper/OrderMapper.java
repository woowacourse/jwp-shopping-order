package cart.persistence.mapper;

import static cart.persistence.mapper.CartMapper.convertCartItems;
import static cart.persistence.mapper.CouponMapper.convertCoupon;
import static cart.persistence.mapper.MemberMapper.convertMember;

import cart.domain.cartitem.CartItem;
import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.domain.order.BasicOrder;
import cart.domain.order.CouponOrder;
import cart.domain.order.Order;
import cart.domain.product.Product;
import cart.persistence.dao.dto.OrderDto;
import cart.persistence.entity.OrderEntity;
import cart.persistence.entity.OrderProductEntity;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderEntity convertOrderEntity(final Order order, final Member member) {
        return new OrderEntity(member.memberId(), order.getTotalPrice(), order.getDiscountedTotalPrice(),
            order.getDeliveryPrice(), order.getOrderedAt());
    }

    public static List<OrderProductEntity> convertOrderProductEntities(final List<CartItem> cartItems,
                                                                       final Long orderId) {
        return cartItems.stream()
            .map(cartItem -> convertOrderProductEntity(orderId, cartItem))
            .collect(Collectors.toUnmodifiableList());
    }

    public static Order convertOrder(final List<OrderDto> orderDto) {
        final OrderDto order = orderDto.get(0);
        final Member member = convertMember(order);
        final List<CartItem> cartItems = convertCartItems(orderDto);

        if (order.getCouponId() == 0) {
            return new BasicOrder(order.getOrderId(), member, order.getDeliveryPrice(),
                order.getOrderedAt(), cartItems, order.getIsValid());
        }
        final Coupon coupon = convertCoupon(order);
        return new CouponOrder(order.getOrderId(), member, coupon, order.getDeliveryPrice(),
            order.getOrderedAt(), cartItems, order.getIsValid());
    }

    private static OrderProductEntity convertOrderProductEntity(final Long orderId,
                                                                final CartItem cartItem) {
        final Product product = cartItem.getProduct();
        return new OrderProductEntity(orderId, product.getProductId(),
            product.getPrice(), cartItem.getQuantity());
    }
}
