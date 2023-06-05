package cart.order.application.mapper;

import cart.coupon.application.dto.CouponResponse;
import cart.member.domain.Member;
import cart.order.application.dto.OrderResponse;
import cart.order.application.dto.RegisterOrderRequest;
import cart.order.application.dto.SpecificOrderResponse;
import cart.order.dao.entity.OrderEntity;
import cart.order.domain.Order;
import cart.order.domain.OrderStatus;
import cart.order_item.application.dto.OrderItemResponse;
import cart.value_object.Money;
import java.time.ZonedDateTime;
import java.util.List;

public class OrderMapper {

  private OrderMapper() {
  }

  public static OrderEntity mapToOrderEntity(
      final Member member,
      final RegisterOrderRequest registerOrderRequest
  ) {
    return new OrderEntity(
        member.getId(),
        registerOrderRequest.getDeliveryFee(),
        registerOrderRequest.getCouponId(),
        OrderStatus.COMPLETE.getValue(),
        ZonedDateTime.now()
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
