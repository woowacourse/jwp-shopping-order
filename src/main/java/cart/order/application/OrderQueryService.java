package cart.order.application;

import cart.member.domain.Member;
import cart.order.application.dto.OrderItemResponse;
import cart.order.application.dto.OrderResponse;
import cart.order.dao.OrderDao;
import cart.order.domain.Order;
import cart.order_item.application.OrderItemQueryService;
import cart.value_object.Money;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderQueryService {

  private final OrderDao orderDao;
  private final OrderItemQueryService orderItemQueryService;

  public OrderQueryService(
      final OrderDao orderDao,
      final OrderItemQueryService orderItemQueryService
  ) {
    this.orderDao = orderDao;
    this.orderItemQueryService = orderItemQueryService;
  }

  public List<OrderResponse> searchOrders(final Member member) {
    final List<Order> orders = orderDao.findByMemberId(member.getId())
        .stream()
        .map(it -> new Order(it.getId(), member, new Money(it.getDeliveryFee())))
        .collect(Collectors.toList());

    return orders.stream()
        .map(order -> new OrderResponse(
            order.getId(),
            mapToOrderItemResponseFrom(order)))
        .collect(Collectors.toList());
  }

  private List<OrderItemResponse> mapToOrderItemResponseFrom(final Order order) {
    return orderItemQueryService.searchOrderItemsByOrderId(order)
        .stream()
        .map(orderItem -> new OrderItemResponse(
            orderItem.getId(),
            orderItem.getName(),
            orderItem.getImageUrl(),
            orderItem.getQuantity(),
            orderItem.getPrice().multiply(orderItem.getQuantity()).getValue()))
        .collect(Collectors.toList());
  }
}
