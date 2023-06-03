package cart.order.dao.entity;

import java.math.BigDecimal;

public class OrderEntity {

  private Long id;
  private Long memberId;
  private BigDecimal deliveryFee;
  private Long couponId;

  public OrderEntity(final Long id, final Long memberId, final BigDecimal deliveryFee,
      final Long couponId) {
    this.id = id;
    this.memberId = memberId;
    this.deliveryFee = deliveryFee;
    this.couponId = couponId;
  }

  public OrderEntity(final Long memberId, final BigDecimal deliveryFee, final Long couponId) {
    this.memberId = memberId;
    this.deliveryFee = deliveryFee;
    this.couponId = couponId;
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

  public Long getCouponId() {
    return couponId;
  }
}
