package cart.infrastructure.entity;

import java.math.BigDecimal;

public class CouponIssueConditionEntity {

    private final Long id;
    private final Long couponId;
    private final BigDecimal minIssuePrice;

    public CouponIssueConditionEntity(Long couponId, BigDecimal minIssuePrice) {
        this(null, couponId, minIssuePrice);
    }

    public CouponIssueConditionEntity(Long id, Long couponId, BigDecimal minIssuePrice) {
        this.id = id;
        this.couponId = couponId;
        this.minIssuePrice = minIssuePrice;
    }

    public Long getId() {
        return id;
    }

    public Long getCouponId() {
        return couponId;
    }

    public BigDecimal getMinIssuePrice() {
        return minIssuePrice;
    }
}
