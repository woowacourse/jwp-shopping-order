package cart.infrastructure.entity;

import java.time.LocalDateTime;

public class OrderEntity {

    private final Long id;
    private final Long memberId;
    private final Long couponId;
    private final String orderNumber;
    private final int deliveryFee;
    private final LocalDateTime createdAt;

    public OrderEntity(Long memberId, int deliveryFee, String orderNumber, LocalDateTime createdAt) {
        this(null, memberId, null, orderNumber, deliveryFee, createdAt);
    }

    public OrderEntity(Long memberId, Long couponId, int deliveryFee, String orderNumber, LocalDateTime createdAt) {
        this(null, memberId, couponId, orderNumber, deliveryFee, createdAt);
    }

    public OrderEntity(Long id, Long memberId, Long couponId, String orderNumber, int deliveryFee,
                       LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.couponId = couponId;
        this.orderNumber = orderNumber;
        this.deliveryFee = deliveryFee;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
