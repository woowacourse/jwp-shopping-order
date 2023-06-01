package cart.order.application;

import cart.member.domain.Member;
import cart.order.application.dto.OrderResponse;
import cart.order.application.dto.SpecificOrderResponse;
import cart.order.application.mapper.OrderMapper;
import cart.order.dao.OrderDao;
import cart.order.dao.entity.OrderEntity;
import cart.order.domain.Order;
import cart.order.exception.CanNotSearchNotMyOrderException;
import cart.order.exception.NotFoundOrderException;
import cart.order_item.application.OrderItemQueryService;
import cart.order_item.application.mapper.OrderItemMapper;
import cart.order_item.domain.OrderItem;
import cart.order_item.domain.OrderedItems;
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
    final List<Order> orders = OrderMapper.mapToOrders(
        member,
        orderDao.findByMemberId(member.getId())
    );

    return orders.stream()
        .map(order -> new OrderResponse(
            order.getId(),
            OrderItemMapper.mapToOrderItemResponse(
                orderItemQueryService.searchOrderItemsByOrderId(order)
            )))
        .collect(Collectors.toList());
  }

  public SpecificOrderResponse searchOrder(final Member member, final Long orderId) {
    final OrderEntity orderEntity = orderDao.findByOrderId(orderId)
        .orElseThrow(() -> new NotFoundOrderException("해당 주문은 존재하지 않습니다."));

    final Order order = OrderMapper.mapToOrder(
        member,
        orderId,
        new Money(orderEntity.getDeliveryFee())
    );

    validateOrderOwner(order, orderEntity.getMemberId());

    final List<OrderItem> orderItems = orderItemQueryService.searchOrderItemsByOrderId(order);
    final OrderedItems orderedItems = new OrderedItems(orderItems);

    return new SpecificOrderResponse(
        orderEntity.getId(),
        OrderItemMapper.mapToOrderItemResponse(orderItems),
        orderedItems.calculateAllItemPrice().getValue(),
        orderEntity.getDeliveryFee()
    );
  }

  private void validateOrderOwner(final Order order, final Long memberId) {
    if (order.isNotMyOrder(memberId)) {
      throw new CanNotSearchNotMyOrderException("사용자의 주문 목록 이외는 조회할 수 없습니다.");
    }
  }
}
