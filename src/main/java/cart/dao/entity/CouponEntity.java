package cart.dao.entity;

import cart.domain.Coupon;

public class CouponEntity {

    private final Long id;
    private final long memberId;
    private final long couponTypeId;
    private final boolean isUsed;

    public CouponEntity(final Long id,
                        final long memberId,
                        final long couponTypeId,
                        final boolean isUsed) {
        this.id = id;
        this.memberId = memberId;
        this.couponTypeId = couponTypeId;
        this.isUsed = isUsed;
    }

    public static CouponEntity from(final Coupon used) {
        return new CouponEntity(
                used.getId(),
                used.getMember().getId(),
                used.getCouponType().getId(),
                used.isUsed());
    }
    
    public Long getId() {
        return id;
    }

    public long getCouponTypeId() {
        return couponTypeId;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public long getMemberId() {
        return memberId;
    }
}
