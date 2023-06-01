package cart.application.mapper;

import static cart.application.mapper.CouponMapper.convertCouponResponse;
import static cart.application.mapper.ProductMapper.convertProductResponse;

import cart.application.dto.coupon.CouponResponse;
import cart.application.dto.order.OrderProductResponse;
import cart.application.dto.order.OrderResponse;
import cart.domain.cartitem.CartItemWithId;
import cart.domain.member.MemberCoupon;
import cart.domain.member.dto.MemberWithId;
import cart.domain.order.BasicOrder;
import cart.domain.order.CouponOrder;
import cart.domain.order.Order;
import cart.domain.order.OrderWithId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static BasicOrder converBasicOrder(final List<CartItemWithId> productWithIds,
                                              final int deliveryPrice, final MemberWithId memberWithId) {
        return new BasicOrder(memberWithId, deliveryPrice, LocalDateTime.now(), productWithIds);
    }

    public static CouponOrder convertCouponOrder(final List<CartItemWithId> productWithIds,
                                                 final int deliveryPrice, final MemberWithId memberWithId,
                                                 final MemberCoupon memberCoupon) {
        return new CouponOrder(memberWithId, memberCoupon.getCoupon(), deliveryPrice,
            LocalDateTime.now(), productWithIds);
    }

    public static List<OrderProductResponse> convertOrderProductResponses(final Order order) {
        return order.getCartItems().stream()
            .map(cartItemWithId -> new OrderProductResponse(cartItemWithId.getQuantity(),
                convertProductResponse(cartItemWithId.getProduct())
            )).collect(Collectors.toUnmodifiableList());
    }

    public static OrderResponse convertOrderResponse(final OrderWithId orderWithId) {
        final Order order = orderWithId.getOrder();
        final List<OrderProductResponse> orderProductResponses = convertOrderProductResponses(order);

        final Long orderId = orderWithId.getOrderId();
        final Integer orderPrice = order.getTotalPrice();
        final Integer discountedTotalPrice = order.getDiscountedTotalPrice();
        final int couponDiscountPrice = orderPrice - discountedTotalPrice;
        final Integer deliveryPrice = order.getDeliveryPrice();
        final LocalDateTime orderedAt = order.getOrderedAt();

        if (order.getCoupon().isPresent()) {
            final CouponResponse couponResponse = convertCouponResponse(order.getCoupon().get());
            return new OrderResponse(orderId, orderPrice, discountedTotalPrice, couponDiscountPrice, deliveryPrice,
                orderedAt, couponResponse, orderProductResponses);
        }
        return new OrderResponse(orderId, orderPrice, discountedTotalPrice, couponDiscountPrice, deliveryPrice,
            orderedAt, null, orderProductResponses);
    }
}
