package shop.persistence;

import java.time.LocalDateTime;

public class OrderCouponDetail {
    private final Long id;
    private final Long orderId;
    private final Long couponId;
    private final String name;
    private final Integer discountRate;
    private final Integer period;
    private final LocalDateTime expiredAt;

    public OrderCouponDetail(Long id, Long orderId, Long couponId, String name, Integer discountRate,
                             Integer period, LocalDateTime expiredAt) {
        this.id = id;
        this.orderId = orderId;
        this.couponId = couponId;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public Integer getPeriod() {
        return period;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }
}
