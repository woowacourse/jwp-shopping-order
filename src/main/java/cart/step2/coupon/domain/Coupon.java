package cart.step2.coupon.domain;

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
