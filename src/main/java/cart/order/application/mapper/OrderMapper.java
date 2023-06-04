package cart.order.application.mapper;

import cart.member.domain.Member;
import cart.order.application.dto.RegisterOrderRequest;
import cart.order.dao.entity.OrderEntity;
import cart.order.domain.Order;
import cart.order.domain.OrderStatus;
import cart.value_object.Money;
import java.time.ZonedDateTime;
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
        registerOrderRequest.getCouponId(),
        OrderStatus.COMPLETE.getValue(),
        ZonedDateTime.now()
    );
  }
}
