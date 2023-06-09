package cart.order.application.mapper;

import cart.coupon.application.dto.CouponResponse;
import cart.member.domain.Member;
import cart.order.application.dto.OrderItemResponse;
import cart.order.application.dto.OrderResponse;
import cart.order.application.dto.SpecificOrderResponse;
import cart.order.dao.entity.OrderEntity;
import cart.order.domain.Order;
import cart.value_object.Money;
import java.util.List;

public class OrderMapper {

  private OrderMapper() {
  }

  public static OrderEntity mapToOrderEntity(
      final Member member,
      final Order order
  ) {
    return new OrderEntity(
        member.getId(),
        order.getDeliveryFee().getValue(),
        order.getCoupon().getId(),
        order.getOrderStatus().getValue(),
        order.getCreatedAt()
    );
  }

  public static OrderResponse mapToOrderResponse(
      final Order order,
      final List<OrderItemResponse> orderItemResponses,
      final Money totalPayments
  ) {
    return new OrderResponse(
        order.getId(),
        orderItemResponses,
        totalPayments.getValue(),
        order.getCreatedAt(),
        order.getOrderStatus().getValue()
    );
  }

  public static SpecificOrderResponse mapToSpecificOrderResponse(
      final Order order, final List<OrderItemResponse> orderItemResponses,
      final Money totalItemPrice, final Money deliveryFee,
      final Money totalPayments, final CouponResponse couponResponse
  ) {
    return new SpecificOrderResponse(
        order.getId(),
        orderItemResponses,
        totalItemPrice.getValue(),
        deliveryFee.getValue(),
        totalPayments.getValue(),
        couponResponse,
        order.getCreatedAt(),
        order.getOrderStatus().getValue()
    );
  }
}
