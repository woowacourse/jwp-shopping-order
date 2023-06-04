package cart.order.application;

import cart.coupon.application.dto.CouponResponse;
import cart.coupon.domain.Coupon;
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
      final Money totalPayments = calculateTotalPayments(order, orderedItems);

      final OrderResponse orderResponse = OrderMapper.mapToOrderResponse(
          order,
          orderItems,
          totalPayments
      );

      orderResponses.add(orderResponse);
    }

    return orderResponses;
  }

  private Money calculateTotalPayments(
      final Order order,
      final OrderedItems orderedItems
  ) {
    final Money totalOrderPrice = orderedItems.calculateAllItemPrice()
        .add(order.getDeliveryFee());

    final Coupon coupon = order.getCoupon();

    return coupon.discount(totalOrderPrice);
  }

  public SpecificOrderResponse searchOrder(final Member member, final Long orderId) {
    Order order = orderDao.findByOrderId(orderId);

    validateOrderOwner(order, member);

    final List<OrderItem> orderItems = orderItemQueryService.searchOrderItemsByOrderId(order);
    final OrderedItems orderedItems = new OrderedItems(orderItems);
    final Money totalPrice = orderedItems.calculateAllItemPrice();
    final Money totalItemPrice = totalPrice.add(order.getDeliveryFee());

    final Coupon coupon = order.getCoupon();
    final Money totalPayments = coupon.discount(totalItemPrice);
    final Money deliveryFee = order.getDeliveryFee();

    final Money priceDiscount = totalPayments.minus(deliveryFee)
        .minus(totalPrice);

    final CouponResponse couponResponse = new CouponResponse(
        coupon.getId(),
        coupon.getName(),
        priceDiscount.getValue()
    );

    return new SpecificOrderResponse(
        order.getId(),
        OrderItemMapper.mapToOrderItemResponse(orderItems),
        totalPrice.getValue(),
        deliveryFee.getValue(),
        totalPayments.getValue(),
        couponResponse
    );
  }

  private void validateOrderOwner(final Order order, final Member member) {
    if (order.isNotMyOrder(member)) {
      throw new CanNotSearchNotMyOrderException("사용자의 주문 목록 이외는 조회할 수 없습니다.");
    }
  }
}
