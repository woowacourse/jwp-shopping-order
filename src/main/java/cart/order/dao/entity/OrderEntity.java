package cart.order.dao.entity;

import java.math.BigDecimal;

public class OrderEntity {

  private Long id;
  private Long memberId;
  private BigDecimal deliveryFee;

  public OrderEntity(final Long id, final Long memberId, final BigDecimal deliveryFee) {
    this.id = id;
    this.memberId = memberId;
    this.deliveryFee = deliveryFee;
  }

  public OrderEntity(final Long memberId, final BigDecimal deliveryFee) {
    this.memberId = memberId;
    this.deliveryFee = deliveryFee;
  }

  public Long getId() {
    return id;
  }

  public Long getMemberId() {
    return memberId;
  }

  public BigDecimal getDeliveryFee() {
    return deliveryFee;
  }
}
