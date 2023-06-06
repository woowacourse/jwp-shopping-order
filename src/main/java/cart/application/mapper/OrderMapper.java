package cart.application.mapper;

import static cart.application.mapper.CouponMapper.convertCouponResponse;
import static cart.application.mapper.ProductMapper.convertProductResponse;

import cart.application.dto.coupon.CouponResponse;
import cart.application.dto.order.OrderProductResponse;
import cart.application.dto.order.OrderResponse;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.domain.order.BasicOrder;
import cart.domain.order.CouponOrder;
import cart.domain.order.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static BasicOrder converBasicOrder(final List<CartItem> cartItems,
                                              final int deliveryPrice, final Member Member) {
        return new BasicOrder(Member, deliveryPrice, LocalDateTime.now(), cartItems, true);
    }

    public static CouponOrder convertCouponOrder(final List<CartItem> cartItems,
                                                 final int deliveryPrice, final Member Member,
                                                 final MemberCoupon memberCoupon) {
        return new CouponOrder(Member, memberCoupon.getCoupon(), deliveryPrice,
            LocalDateTime.now(), cartItems, true);
    }

    public static List<OrderProductResponse> convertOrderProductResponses(final Order order) {
        return order.getCartItems().stream()
            .map(cartItem -> new OrderProductResponse(cartItem.getQuantity(),
                convertProductResponse(cartItem.getProduct())
            )).collect(Collectors.toUnmodifiableList());
    }

    public static OrderResponse convertOrderResponse(final Order order) {
        final List<OrderProductResponse> orderProductResponses = convertOrderProductResponses(order);

        final Long orderId = order.getOrderId();
        final BigDecimal orderPrice = order.getTotalPrice();
        final BigDecimal discountedTotalPrice = order.getDiscountedTotalPrice();
        final BigDecimal couponDiscountPrice = orderPrice.subtract(discountedTotalPrice);
        final Integer deliveryPrice = order.getDeliveryPrice();
        final LocalDateTime orderedAt = order.getOrderedAt();
        final Boolean isValid = order.isValid();

        if (order.getCoupon().isPresent()) {
            final CouponResponse couponResponse = convertCouponResponse(order.getCoupon().get());
            return new OrderResponse(orderId, orderPrice, discountedTotalPrice, couponDiscountPrice, deliveryPrice,
                orderedAt, couponResponse, orderProductResponses, isValid);
        }
        return new OrderResponse(orderId, orderPrice, discountedTotalPrice, couponDiscountPrice, deliveryPrice,
            orderedAt, null, orderProductResponses, isValid);
    }
}
