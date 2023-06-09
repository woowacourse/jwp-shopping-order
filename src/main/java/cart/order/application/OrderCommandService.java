package cart.order.application;

import cart.cart_item.application.CartItemService;
import cart.cart_item.application.dto.RemoveCartItemRequest;
import cart.cart_item.domain.CartItem;
import cart.coupon.application.CouponCommandService;
import cart.coupon.application.CouponQueryService;
import cart.coupon.domain.Coupon;
import cart.member.domain.Member;
import cart.order.application.dto.RegisterOrderRequest;
import cart.order.application.mapper.OrderItemMapper;
import cart.order.application.mapper.OrderMapper;
import cart.order.dao.OrderDao;
import cart.order.dao.OrderItemDao;
import cart.order.dao.entity.OrderEntity;
import cart.order.domain.Order;
import cart.order.domain.OrderItem;
import cart.order.domain.OrderStatus;
import cart.order.domain.OrderedItems;
import cart.order.exception.OrderException;
import cart.order.exception.OrderExceptionType;
import cart.value_object.Money;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderCommandService {

  private final OrderDao orderDao;
  private final OrderItemDao orderItemDao;
  private final CartItemService cartItemService;
  private final CouponCommandService couponCommandService;
  private final CouponQueryService couponQueryService;

  public OrderCommandService(
      final OrderDao orderDao, final OrderItemDao orderItemDao,
      final CartItemService cartItemService,
      final CouponCommandService couponCommandService,
      final CouponQueryService couponQueryService
  ) {
    this.orderDao = orderDao;
    this.orderItemDao = orderItemDao;
    this.cartItemService = cartItemService;
    this.couponCommandService = couponCommandService;
    this.couponQueryService = couponQueryService;
  }

  public Long registerOrder(final Member member, final RegisterOrderRequest registerOrderRequest) {

    final List<CartItem> cartItems = removeCartItemsFromOrder(member, registerOrderRequest);

    final OrderedItems orderedItems = OrderedItems.createdFromOrder(
        OrderItemMapper.mapToOrderItemsFrom(cartItems),
        new Money(registerOrderRequest.getTotalPrice())
    );

    final Coupon coupon = couponQueryService.searchCouponOwnedByMember(
        member,
        registerOrderRequest.getCouponId()
    );

    final Order order = new Order(
        member,
        new Money(registerOrderRequest.getDeliveryFee()),
        coupon,
        orderedItems
    );

    if (order.hasCoupon()) {
      couponCommandService.updateUsedCoupon(coupon, member);
    }

    return saveOrder(member, order, orderedItems);
  }

  private List<CartItem> removeCartItemsFromOrder(
      final Member member,
      final RegisterOrderRequest registerOrderRequest
  ) {
    final List<Long> cartItemIds = registerOrderRequest.getCartItemIds();
    final List<CartItem> cartItems = cartItemService.findCartItemByCartIds(
        cartItemIds,
        member
    );
    cartItemService.removeBatch(member, new RemoveCartItemRequest(cartItemIds));
    return cartItems;
  }

  private Long saveOrder(final Member member, final Order order, final OrderedItems orderedItems) {
    final OrderEntity orderEntity = OrderMapper.mapToOrderEntity(member, order);
    final Long savedOrderId = orderDao.save(orderEntity);
    orderItemDao.save(OrderItemMapper.mapToOrderItemEntities(orderedItems, savedOrderId));

    return savedOrderId;
  }

  public void deleteOrder(final Member member, final Long orderId) {
    final Order order = orderDao.findByOrderId(orderId);
    validateOrderOwner(order, member);

    final List<Long> deletedOrderItemIds = orderItemDao.findByOrderId(orderId)
        .stream()
        .map(OrderItem::getId)
        .collect(Collectors.toList());

    orderItemDao.deleteBatch(deletedOrderItemIds);
    orderDao.deleteByOrderId(orderId);
  }

  private void validateOrderOwner(final Order order, final Member member) {
    if (order.isNotMyOrder(member)) {
      throw new OrderException(OrderExceptionType.CAN_NOT_CHANGE_NOT_MY_ORDER);
    }
  }

  public void updateToOrderCancel(final Member member, final Long orderId) {
    orderDao.updateByOrderId(orderId, OrderStatus.CANCEL.getValue());

    final Order order = orderDao.findByOrderId(orderId);
    validateOrderOwner(order, member);

    if (order.hasCoupon()) {
      couponCommandService.updateUsedCoupon(order.getCoupon(), member);
    }
  }
}
