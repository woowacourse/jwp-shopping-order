package cart.order.application.mapper;

import cart.member.domain.Member;
import cart.order.application.dto.RegisterOrderRequest;
import cart.order.dao.entity.OrderEntity;
import cart.order.domain.Order;
import cart.value_object.Money;
import java.util.List;
import java.util.stream.Collectors;

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
        registerOrderRequest.getCouponId()
    );
  }

  public static Order mapToOrder(
      final Member member,
      final Long savedOrderId,
      final RegisterOrderRequest registerOrderRequest
  ) {
    return new Order(savedOrderId, member, new Money(registerOrderRequest.getDeliveryFee()));
  }

  public static Order mapToOrder(
      final Member member,
      final Long savedOrderId,
      final Money deliveryFee
  ) {
    return new Order(savedOrderId, member, deliveryFee);
  }

  public static List<Order> mapToOrders(
      final Member member,
      final List<OrderEntity> orderEntities
  ) {
    return orderEntities.stream()
        .map(orderEntity -> new Order(
            orderEntity.getId(),
            member,
            new Money(orderEntity.getDeliveryFee())))
        .collect(Collectors.toList());
  }
}
