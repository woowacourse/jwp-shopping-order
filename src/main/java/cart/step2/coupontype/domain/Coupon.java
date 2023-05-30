package cart.step2.coupontype.domain;

public class Coupon {

    private final Long id;
    private final String usageStatus;
    private final Long memberId;
    private final Long couponTypeId;

    public Coupon(final Long id, final String usageStatus, final Long memberId, final Long couponTypeId) {
        this.id = id;
        this.usageStatus = usageStatus;
        this.memberId = memberId;
        this.couponTypeId = couponTypeId;
    }

    public Long getId() {
        return id;
    }

    public String getUsageStatus() {
        return usageStatus;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getCouponTypeId() {
        return couponTypeId;
    }
}
