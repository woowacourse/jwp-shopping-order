package cart.order.domain;

import cart.member.domain.Member;
import cart.value_object.Money;

public class Order {

  private Long id;

  private Member member;

  private Money deliveryFee;

  public Order(final Long id, final Member member, final Money deliveryFee) {
    this.id = id;
    this.member = member;
    this.deliveryFee = deliveryFee;
  }

  public boolean isNotMyOrder(final Long memberId) {
    return !this.member.isMe(memberId);
  }

  public Long getId() {
    return id;
  }
}
