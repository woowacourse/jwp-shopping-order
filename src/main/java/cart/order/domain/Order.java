package cart.order.domain;

import cart.coupon.domain.Coupon;
import cart.member.domain.Member;
import cart.value_object.Money;
import java.time.ZonedDateTime;

public class Order {

  private Long id;

  private final Member member;

  private final Money deliveryFee;

  private final Coupon coupon;

  private final OrderStatus orderStatus;

  private final ZonedDateTime createdAt;

  public Order(
      final Long id, final Member member,
      final Money deliveryFee, final Coupon coupon,
      final OrderStatus orderStatus, final ZonedDateTime createdAt
  ) {
    this.id = id;
    this.member = member;
    this.deliveryFee = deliveryFee;
    this.coupon = coupon;
    this.orderStatus = orderStatus;
    this.createdAt = createdAt;
  }

  public Order(
      final Member member,
      final Money deliveryFee,
      final Coupon coupon
  ) {
    this.member = member;
    this.deliveryFee = deliveryFee;
    this.coupon = coupon;
    this.createdAt = ZonedDateTime.now();
    this.orderStatus = OrderStatus.COMPLETE;
  }

  public boolean isNotMyOrder(final Member member) {
    return !this.member.isMe(member);
  }

  public Money calculateTotalPayments(final Money totalItemPrice) {
    final Money totalOrderPrice = totalItemPrice.add(deliveryFee);
    return coupon.discount(totalOrderPrice);
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
