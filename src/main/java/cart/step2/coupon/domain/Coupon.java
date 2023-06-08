package cart.step2.coupon.domain;

import cart.step2.coupon.exception.UnusedCouponsCannotDeleted;

public class Coupon {

    private final Long id;
    private final Integer usageStatus;
    private final Long memberId;
    private final Long couponTypeId;

    public Coupon(final Long id, final Integer usageStatus, final Long memberId, final Long couponTypeId) {
        this.id = id;
        this.usageStatus = usageStatus;
        this.memberId = memberId;
        this.couponTypeId = couponTypeId;
    }

    public void validateUsageStatus() {
        if (usageStatus.equals(0)) {
            throw UnusedCouponsCannotDeleted.THROW;
        }
    }

    public Long getId() {
        return id;
    }

    public Integer getUsageStatus() {
        return usageStatus;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getCouponTypeId() {
        return couponTypeId;
    }
}
