package cart.order.application;

import cart.coupon.application.mapper.CouponMapper;
import cart.member.domain.Member;
import cart.order.application.dto.OrderResponse;
import cart.order.application.dto.SpecificOrderResponse;
import cart.order.application.mapper.OrderMapper;
import cart.order.dao.OrderDao;
import cart.order.domain.Order;
import cart.order.exception.CanNotSearchNotMyOrderException;
import cart.order_item.application.OrderItemQueryService;
import cart.order_item.application.mapper.OrderItemMapper;
import cart.order_item.domain.OrderItem;
import cart.order_item.domain.OrderedItems;
import cart.value_object.Money;
import java.util.ArrayList;
import java.util.List;
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
    final List<OrderResponse> orderResponses = new ArrayList<>();

    for (final Order order : orders) {
      validateOrderOwner(order, member);

      final List<OrderItem> orderItems = orderItemQueryService.searchOrderItemsByOrderId(order);
      final OrderedItems orderedItems = new OrderedItems(orderItems);

      final Money totalPayments = order.calculateTotalPayments(
          orderedItems.calculateAllItemPrice()
      );

      final OrderResponse orderResponse = OrderMapper.mapToOrderResponse(
          order,
          OrderItemMapper.mapToOrderItemResponse(orderItems),
          totalPayments
      );

      orderResponses.add(orderResponse);
    }

    return orderResponses;
  }

  public SpecificOrderResponse searchOrder(final Member member, final Long orderId) {
    Order order = orderDao.findByOrderId(orderId);
    validateOrderOwner(order, member);

    final List<OrderItem> orderItems = orderItemQueryService.searchOrderItemsByOrderId(order);
    final OrderedItems orderedItems = new OrderedItems(orderItems);
    final Money totalItemPrice = orderedItems.calculateAllItemPrice();

    final Money totalPayments = order.calculateTotalPayments(totalItemPrice);
    final Money deliveryFee = order.getDeliveryFee();

    return OrderMapper.mapToSpecificOrderResponse(
        order, OrderItemMapper.mapToOrderItemResponse(orderItems),
        totalItemPrice, deliveryFee,
        totalPayments, CouponMapper.mapToCouponResponse(order.getCoupon())
    );
  }

  private void validateOrderOwner(final Order order, final Member member) {
    if (order.isNotMyOrder(member)) {
      throw new CanNotSearchNotMyOrderException("사용자의 주문 목록 이외는 조회할 수 없습니다.");
    }
  }
}
