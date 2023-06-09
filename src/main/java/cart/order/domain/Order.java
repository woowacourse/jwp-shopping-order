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
    this.id = id;
    this.member = member;
    this.deliveryFee = deliveryFee;
    this.coupon = coupon;
    this.orderStatus = orderStatus;
    this.createdAt = createdAt;
    this.orderedItems = orderedItems;
  }

  public Order(
      final Member member,
      final Money deliveryFee,
      final Coupon coupon,
      final OrderedItems orderedItems
  ) {
    this.member = member;
    this.deliveryFee = deliveryFee;
    this.coupon = coupon;
    this.createdAt = ZonedDateTime.now();
    this.orderStatus = OrderStatus.COMPLETE;
    this.orderedItems = orderedItems;
  }

  public boolean isNotMyOrder(final Member member) {
    return !this.member.isMe(member);
  }

  public Money calculateTotalPayments(final Money totalItemPrice) {
    final Money totalOrderPrice = totalItemPrice.add(deliveryFee);
    return coupon.discount(totalOrderPrice);
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
}
