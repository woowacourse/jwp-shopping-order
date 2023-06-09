package cart.order.dao.entity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class OrderEntity {

  private Long id;
  private Long memberId;
  private BigDecimal deliveryFee;
  private Long couponId;
  private String orderStatus;
  private ZonedDateTime createdAt;

  public OrderEntity(
      final Long id, final Long memberId,
      final BigDecimal deliveryFee, final Long couponId,
      final String orderStatus, final ZonedDateTime createdAt
  ) {
    this.id = id;
    this.memberId = memberId;
    this.deliveryFee = deliveryFee;
    this.couponId = couponId;
    this.orderStatus = orderStatus;
    this.createdAt = createdAt;
  }

  public OrderEntity(
      final Long memberId, final BigDecimal deliveryFee,
      final Long couponId, final String orderStatus,
      final ZonedDateTime createdAt
  ) {
    this.memberId = memberId;
    this.deliveryFee = deliveryFee;
    this.couponId = couponId;
    this.orderStatus = orderStatus;
    this.createdAt = createdAt;
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

  public String getOrderStatus() {
    return orderStatus;
  }

  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }
}
