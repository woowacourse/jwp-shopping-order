package cart.order.application;

import cart.cart_item.application.CartItemService;
import cart.cart_item.application.dto.RemoveCartItemRequest;
import cart.coupon.domain.Coupon;
import cart.member.domain.Member;
import cart.member_coupon.application.MemberCouponCommandService;
import cart.order.application.dto.RegisterOrderRequest;
import cart.order.application.mapper.OrderMapper;
import cart.order.dao.OrderDao;
import cart.order.dao.entity.OrderEntity;
import cart.order.domain.Order;
import cart.order.domain.OrderStatus;
import cart.order.exception.CanNotDeleteNotMyOrderException;
import cart.order.exception.CanNotDiscountPriceMoreThanTotalPrice;
import cart.order.exception.NotSameTotalPriceException;
import cart.order_item.application.OrderItemCommandService;
import cart.order_item.domain.OrderedItems;
import cart.value_object.Money;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderCommandService {

  private final OrderDao orderDao;
  private final OrderItemCommandService orderItemCommandService;
  private final CartItemService cartItemService;
  private final MemberCouponCommandService memberCouponCommandService;

  public OrderCommandService(
      final OrderDao orderDao,
      final OrderItemCommandService orderItemCommandService,
      final CartItemService cartItemService,
      final MemberCouponCommandService memberCouponCommandService
  ) {
    this.orderDao = orderDao;
    this.orderItemCommandService = orderItemCommandService;
    this.cartItemService = cartItemService;
    this.memberCouponCommandService = memberCouponCommandService;
  }

  public Long registerOrder(final Member member, final RegisterOrderRequest registerOrderRequest) {

    final List<Long> cartItemIds = registerOrderRequest.getCartItemIds();
    final Money totalPrice = new Money(registerOrderRequest.getTotalPrice());

    final OrderEntity orderEntity = OrderMapper.mapToOrderEntity(member, registerOrderRequest);
    final Long savedOrderId = orderDao.save(orderEntity);

    final Order order = orderDao.findByOrderId(savedOrderId);

    final OrderedItems orderedItems = orderItemCommandService.registerOrderItem(
        cartItemIds,
        order,
        member
    );
    final Money orderItemsTotalPrice = orderedItems.calculateAllItemPrice();
    validateSameTotalPrice(orderItemsTotalPrice, totalPrice);

    final Coupon coupon = order.getCoupon();
    validateCouponExceedTotalPrice(orderedItems, coupon);

    cartItemService.removeBatch(member, new RemoveCartItemRequest(cartItemIds));

    if (hasCouponWith(registerOrderRequest)) {
      memberCouponCommandService.updateUsedCoupon(
          registerOrderRequest.getCouponId(),
          member.getId()
      );
    }

    return savedOrderId;
  }

  private boolean hasCouponWith(final RegisterOrderRequest registerOrderRequest) {
    return registerOrderRequest.getCouponId() != null;
  }

  private void validateCouponExceedTotalPrice(final OrderedItems orderedItems,
      final Coupon coupon) {
    if (coupon.isExceedDiscountFrom(orderedItems.calculateAllItemPrice())) {
      throw new CanNotDiscountPriceMoreThanTotalPrice("쿠폰 가격이 전체 가격보다 높으면 사용할 수 있습니다.");
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

    orderDao.deleteByOrderId(orderId);
    orderItemCommandService.deleteBatch(order);
  }

  private void validateOrderOwner(final Order order, final Member member) {
    if (order.isNotMyOrder(member)) {
      throw new CanNotDeleteNotMyOrderException("사용자의 주문 목록 이외는 삭제할 수 없습니다.");
    }
  }

  public void updateOrder(final Member member, final Long orderId) {
    //주문 취소로 변경
    orderDao.updateOrderByOrderId(orderId, OrderStatus.CANCEL.getValue());

    //쿠폰 다시 그대로 변경
    final Order order = orderDao.findByOrderId(orderId);

    final Coupon usedCoupon = order.getCoupon();

    memberCouponCommandService.updateUsedCoupon(usedCoupon.getId(), member.getId());
  }
}
