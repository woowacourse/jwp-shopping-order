package cart.dao.entity;

public class CouponEntity {

    private final Long id;
    private final long couponTypeId;
    private final boolean isUsed;

    public CouponEntity(final Long id, final long couponTypeId, final boolean isUsed) {
        this.id = id;
        this.couponTypeId = couponTypeId;
        this.isUsed = isUsed;
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
}
