package cart.order.domain;

import cart.coupon.domain.Coupon;
import cart.member.domain.Member;
import cart.value_object.Money;
import java.time.ZonedDateTime;

public class Order {

  private Long id;

  private Member member;

  private Money deliveryFee;

  private Coupon coupon;

  private OrderStatus orderStatus;
  private ZonedDateTime createdAt;

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

  public Long getId() {
    return id;
  }

  public Money getDeliveryFee() {
    return deliveryFee;
  }

  public Coupon getCoupon() {
    return coupon;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }

  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }
}
