package cart.order.application;

import cart.member.domain.Member;
import cart.order.application.dto.OrderResponse;
import cart.order.application.dto.SpecificOrderResponse;
import cart.order.dao.OrderDao;
import cart.order.domain.Order;
import cart.order.exception.CanNotSearchNotMyOrderException;
import cart.order_item.application.OrderItemQueryService;
import cart.order_item.application.mapper.OrderItemMapper;
import cart.order_item.domain.OrderItem;
import cart.order_item.domain.OrderedItems;
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
    final List<Order> orders = orderDao.findByMemberId(member.getId());

    return orders.stream()
        .map(order -> new OrderResponse(
            order.getId(),
            OrderItemMapper.mapToOrderItemResponse(
                orderItemQueryService.searchOrderItemsByOrderId(order)
            )))
        .collect(Collectors.toList());
  }

  public SpecificOrderResponse searchOrder(final Member member, final Long orderId) {
    Order order = orderDao.findByOrderId(orderId);

    validateOrderOwner(order, member);

    final List<OrderItem> orderItems = orderItemQueryService.searchOrderItemsByOrderId(order);
    final OrderedItems orderedItems = new OrderedItems(orderItems);

    return new SpecificOrderResponse(
        order.getId(),
        OrderItemMapper.mapToOrderItemResponse(orderItems),
        orderedItems.calculateAllItemPrice().getValue(),
        order.getDeliveryFee().getValue()
    );
  }

  private void validateOrderOwner(final Order order, final Member member) {
    if (order.isNotMyOrder(member)) {
      throw new CanNotSearchNotMyOrderException("사용자의 주문 목록 이외는 조회할 수 없습니다.");
    }
  }
}
