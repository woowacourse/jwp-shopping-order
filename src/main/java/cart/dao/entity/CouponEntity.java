package cart.dao.entity;

public class CouponEntity {

    private final Long id;
    private final boolean usageStatus;
    private final long memberId;
    private final long couponTypeId;

    public CouponEntity(final boolean usageStatus, final long memberId, final long couponTypeId) {
        this(null, usageStatus, memberId, couponTypeId);
    }

    public CouponEntity(final Long id, final boolean usageStatus, final long memberId, final long couponTypeId) {
        this.id = id;
        this.usageStatus = usageStatus;
        this.memberId = memberId;
        this.couponTypeId = couponTypeId;
    }

    public static CouponEntity of(final Long memberId, final Long couponId) {
        return new CouponEntity(false, memberId, couponId);
    }

    public Long getId() {
        return id;
    }

    public boolean isUsageStatus() {
        return usageStatus;
    }

    public long getMemberId() {
        return memberId;
    }

    public long getCouponTypeId() {
        return couponTypeId;
    }
}
