package cart.order.application;

import cart.coupon.application.mapper.CouponMapper;
import cart.member.domain.Member;
import cart.order.application.dto.OrderResponse;
import cart.order.application.dto.SpecificOrderResponse;
import cart.order.application.mapper.OrderItemMapper;
import cart.order.application.mapper.OrderMapper;
import cart.order.dao.OrderDao;
import cart.order.domain.Order;
import cart.order.exception.enum_exception.OrderException;
import cart.order.exception.enum_exception.OrderExceptionType;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderQueryService {

  private final OrderDao orderDao;

  public OrderQueryService(final OrderDao orderDao) {
    this.orderDao = orderDao;
  }

  public List<OrderResponse> searchOrders(final Member member) {
    return orderDao.findByMemberId(member.getId()).stream()
        .peek(order -> validateOrderOwner(order, member))
        .map(order -> OrderMapper.mapToOrderResponse(
            order,
            OrderItemMapper.mapToOrderItemResponse(order.getOrderedItems())))
        .sorted(Comparator.comparing(OrderResponse::getCreatedAt).reversed())
        .collect(Collectors.toList());
  }

  public SpecificOrderResponse searchOrder(final Member member, final Long orderId) {
    Order order = orderDao.findByOrderId(orderId);
    validateOrderOwner(order, member);

    return OrderMapper.mapToSpecificOrderResponse(
        order,
        OrderItemMapper.mapToOrderItemResponse(order.getOrderedItems()),
        CouponMapper.mapToCouponResponse(order.getCoupon())
    );
  }

  private void validateOrderOwner(final Order order, final Member member) {
    if (order.isNotMyOrder(member)) {
      throw new OrderException(OrderExceptionType.CAN_NOT_SEARCH_NOT_MY_ORDER);
    }
  }
}
