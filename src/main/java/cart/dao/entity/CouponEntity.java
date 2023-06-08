package cart.dao.entity;

import cart.domain.Coupon;
import cart.domain.CouponType;

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

    public static CouponEntity from(final long memberId, final Coupon used) {
        return new CouponEntity(
                used.getId(),
                memberId,
                used.getCouponType().getId(),
                used.isUsed());
    }

    public Coupon create(final CouponType couponType) {
        return new Coupon(id, couponType, isUsed);
    }

    public Long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public long getCouponTypeId() {
        return couponTypeId;
    }

    public boolean isUsed() {
        return isUsed;
    }
}
