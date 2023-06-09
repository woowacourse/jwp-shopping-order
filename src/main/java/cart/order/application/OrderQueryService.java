package cart.order.application;

import cart.coupon.application.mapper.CouponMapper;
import cart.member.domain.Member;
import cart.order.application.dto.OrderResponse;
import cart.order.application.dto.SpecificOrderResponse;
import cart.order.application.mapper.OrderItemMapper;
import cart.order.application.mapper.OrderMapper;
import cart.order.dao.OrderDao;
import cart.order.dao.OrderItemDao;
import cart.order.domain.Order;
import cart.order.domain.OrderItem;
import cart.order.domain.OrderedItems;
import cart.order.exception.enum_exception.OrderException;
import cart.order.exception.enum_exception.OrderExceptionType;
import cart.value_object.Money;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderQueryService {

  private final OrderDao orderDao;
  private final OrderItemDao orderItemDao;

  public OrderQueryService(final OrderDao orderDao, final OrderItemDao orderItemDao) {
    this.orderDao = orderDao;
    this.orderItemDao = orderItemDao;
  }

  public List<OrderResponse> searchOrders(final Member member) {
    final List<Order> orders = orderDao.findByMemberId(member.getId());
    orders.sort(Comparator.comparing(Order::getCreatedAt).reversed());

    final List<OrderResponse> orderResponses = new ArrayList<>();

    for (final Order order : orders) {
      validateOrderOwner(order, member);

      final List<OrderItem> orderItems = orderItemDao.findByOrderId(order.getId());
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

    final List<OrderItem> orderItems = orderItemDao.findByOrderId(order.getId());
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
      throw new OrderException(OrderExceptionType.CAN_NOT_SEARCH_NOT_MY_ORDER);
    }
  }
}
