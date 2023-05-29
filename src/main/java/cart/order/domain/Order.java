package cart.order.domain;

import cart.member.domain.Member;
import cart.value_object.Money;

public class Order {

  private Long id;

  private Member member;

  private Money deliveryFee;
}
