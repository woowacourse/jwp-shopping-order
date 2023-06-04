package cart.order.application.mapper;

import cart.member.domain.Member;
import cart.order.application.dto.OrderResponse;
import cart.order.application.dto.RegisterOrderRequest;
import cart.order.dao.entity.OrderEntity;
import cart.order.domain.Order;
import cart.order.domain.OrderStatus;
import cart.order_item.application.mapper.OrderItemMapper;
import cart.order_item.domain.OrderItem;
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
      final List<OrderItem> orderItems,
      final Money totalPayments
  ) {
    return new OrderResponse(
        order.getId(),
        OrderItemMapper.mapToOrderItemResponse(orderItems),
        totalPayments.getValue(),
        order.getCreatedAt(),
        order.getOrderStatus().getValue()
    );
  }
}
