package cart.order.application;

import cart.cart_item.application.CartItemService;
import cart.cart_item.application.dto.RemoveCartItemRequest;
import cart.cart_item.domain.CartItem;
import cart.coupon.domain.Coupon;
import cart.member.domain.Member;
import cart.member_coupon.application.MemberCouponCommandService;
import cart.order.application.dto.RegisterOrderRequest;
import cart.order.application.mapper.OrderItemMapper;
import cart.order.application.mapper.OrderMapper;
import cart.order.dao.OrderDao;
import cart.order.dao.OrderItemDao;
import cart.order.dao.entity.OrderEntity;
import cart.order.dao.entity.OrderItemEntity;
import cart.order.domain.Order;
import cart.order.domain.OrderItem;
import cart.order.domain.OrderStatus;
import cart.order.domain.OrderedItems;
import cart.order.exception.CanNotChangeNotMyOrderException;
import cart.order.exception.CanNotDiscountPriceMoreThanTotalPriceException;
import cart.order.exception.CanNotOrderNotInCartException;
import cart.order.exception.NotSameTotalPriceException;
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
  private final MemberCouponCommandService memberCouponCommandService;

  public OrderCommandService(
      final OrderDao orderDao,
      final OrderItemDao orderItemDao,
      final CartItemService cartItemService,
      final MemberCouponCommandService memberCouponCommandService
  ) {
    this.orderDao = orderDao;
    this.orderItemDao = orderItemDao;
    this.cartItemService = cartItemService;
    this.memberCouponCommandService = memberCouponCommandService;
  }

  public Long registerOrder(final Member member, final RegisterOrderRequest registerOrderRequest) {

    final List<Long> cartItemIds = registerOrderRequest.getCartItemIds();
    final Money totalPrice = new Money(registerOrderRequest.getTotalPrice());

    final OrderEntity orderEntity = OrderMapper.mapToOrderEntity(member, registerOrderRequest);
    final Long savedOrderId = orderDao.save(orderEntity);

    final Order order = orderDao.findByOrderId(savedOrderId);

    final OrderedItems orderedItems = registerOrderItem(cartItemIds, order, member);
    final Money orderItemsTotalPrice = orderedItems.calculateAllItemPrice();
    validateSameTotalPrice(orderItemsTotalPrice, totalPrice);

    final Coupon coupon = order.getCoupon();
    validateCouponExceedTotalPrice(orderedItems, coupon);

    cartItemService.removeBatch(member, new RemoveCartItemRequest(cartItemIds));

    if (order.hasCoupon()) {
      memberCouponCommandService.updateUsedCoupon(coupon, member);
    }

    return savedOrderId;
  }

  private OrderedItems registerOrderItem(
      final List<Long> cartItemIds,
      final Order order,
      final Member member
  ) {
    final List<CartItem> cartItems = cartItemService.findCartItemByCartIds(cartItemIds, member);
    validateAllCartItemInOrder(cartItemIds, cartItems);

    final List<OrderItemEntity> orderItemEntities = OrderItemMapper.mapToOrderItemEntities(
        cartItems,
        order
    );
    orderItemDao.save(orderItemEntities);

    final List<OrderItem> orderItems = orderItemDao.findByOrderId(order.getId());
    return new OrderedItems(orderItems);
  }

  private void validateAllCartItemInOrder(
      final List<Long> cartItemIds,
      final List<CartItem> cartItems
  ) {
    if (cartItemIds.size() != cartItems.size()) {
      throw new CanNotOrderNotInCartException("장바구니에 담지 않은 물품은 주문할 수 없습니다.");
    }
  }

  private void validateCouponExceedTotalPrice(
      final OrderedItems orderedItems,
      final Coupon coupon
  ) {
    if (coupon.isExceedDiscountFrom(orderedItems.calculateAllItemPrice())) {
      throw new CanNotDiscountPriceMoreThanTotalPriceException("쿠폰 가격이 전체 가격보다 높으면 사용할 수 있습니다.");
    }
  }

  private void validateSameTotalPrice(final Money totalPrice, final Money targetTotalPrice) {
    if (totalPrice.isNotSame(targetTotalPrice)) {
      throw new NotSameTotalPriceException("주문된 총액이 올바르지 않습니다.");
    }
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
      throw new CanNotChangeNotMyOrderException("사용자의 주문 목록 이외는 수정할 수 없습니다.");
    }
  }

  public void updateToOrderCancel(final Member member, final Long orderId) {
    orderDao.updateByOrderId(orderId, OrderStatus.CANCEL.getValue());

    final Order order = orderDao.findByOrderId(orderId);
    validateOrderOwner(order, member);

    if (order.hasCoupon()) {
      memberCouponCommandService.updateUsedCoupon(order.getCoupon(), member);
    }
  }
}
