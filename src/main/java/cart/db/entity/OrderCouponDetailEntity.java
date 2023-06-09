package cart.db.entity;

import java.time.LocalDateTime;

public class OrderCouponDetailEntity {

    private Long id;
    private final Long orderId;
    private final Long couponId;
    private final String couponName;
    private final int discountRate;
    private final int period;
    private final LocalDateTime expiredAt;

    public OrderCouponDetailEntity(
            final Long id,
            final Long orderId,
            final Long couponId,
            final String couponName,
            final int discountRate,
            final int period,
            final LocalDateTime expiredAt
    ) {
        this.id = id;
        this.orderId = orderId;
        this.couponId = couponId;
        this.couponName = couponName;
        this.discountRate = discountRate;
        this.period = period;
        this.expiredAt = expiredAt;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public int getPeriod() {
        return period;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }
}
