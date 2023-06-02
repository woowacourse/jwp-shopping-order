package cart.order.domain;

import cart.coupon.domain.Coupon;
import cart.member.domain.Member;
import cart.value_object.Money;

public class Order {

  private Long id;

  private Member member;

  private Money deliveryFee;

  private Coupon coupon;

  public Order(final Long id, final Member member, final Money deliveryFee, final Coupon coupon) {
    this.id = id;
    this.member = member;
    this.deliveryFee = deliveryFee;
    this.coupon = coupon;
  }

  public Order(final Long id, final Member member, final Money deliveryFee) {
    this.id = id;
    this.member = member;
    this.deliveryFee = deliveryFee;
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
}
