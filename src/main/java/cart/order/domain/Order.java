package cart.order.domain;

import cart.coupon.domain.Coupon;
import cart.member.domain.Member;
import cart.value_object.Money;
import java.time.ZonedDateTime;

public class Order {

  private Long id;

  private final Member member;

  private final OrderedItems orderedItems;

  private final Money deliveryFee;

  private final Coupon coupon;

  private final OrderStatus orderStatus;

  private final ZonedDateTime createdAt;

  public Order(
      final Long id, final Member member,
      final Money deliveryFee, final Coupon coupon,
      final OrderStatus orderStatus, final ZonedDateTime createdAt,
      final OrderedItems orderedItems
  ) {
    validateCouponExceedTotalPrice(coupon, orderedItems);
    this.id = id;
    this.member = member;
    this.deliveryFee = deliveryFee;
    this.coupon = coupon;
    this.orderStatus = orderStatus;
    this.createdAt = createdAt;
    this.orderedItems = orderedItems;
  }

  public Order(
      final Member member, final Money deliveryFee,
      final Coupon coupon, final OrderedItems orderedItems
  ) {
    validateCouponExceedTotalPrice(coupon, orderedItems);
    this.member = member;
    this.deliveryFee = deliveryFee;
    this.coupon = coupon;
    this.orderedItems = orderedItems;
    this.createdAt = ZonedDateTime.now();
    this.orderStatus = OrderStatus.COMPLETE;
  }

  private void validateCouponExceedTotalPrice(
      final Coupon coupon,
      final OrderedItems orderedItems
  ) {
    if (coupon.isExceedDiscountFrom(orderedItems.calculateAllItemPrice())) {
      throw new IllegalArgumentException("쿠폰 가격이 전체 가격보다 높으면 사용할 수 없습니다.");
    }
  }

  public boolean isNotMyOrder(final Member member) {
    return !this.member.isMe(member);
  }

  public Money calculateTotalPayments() {
    return coupon.discount(orderedItems.calculateAllItemPrice());
  }

  public Money calculateTotalPrice() {
    return orderedItems.calculateAllItemPrice();
  }

  public boolean hasCoupon() {
    return coupon.isExisted();
  }

  public Long getId() {
    return id;
  }

  public Money getDeliveryFee() {
    return deliveryFee;
  }

  public Coupon getCoupon() {
    return coupon;
  }

  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }

  public OrderedItems getOrderedItems() {
    return orderedItems;
  }
}
