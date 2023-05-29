package cart.dao.entity;

public class CouponTypeCouponEntity {

    private final Long couponTypeId;
    private final Long couponId;
    private final String name;
    private final String description;
    private final int discountAmount;
    private final boolean usageStatus;

    public CouponTypeCouponEntity(
            final Long couponTypeId,
            final Long couponId,
            final String name,
            final String description,
            final int discountAmount,
            final boolean usageStatus
    ) {
        this.couponTypeId = couponTypeId;
        this.couponId = couponId;
        this.name = name;
        this.description = description;
        this.discountAmount = discountAmount;
        this.usageStatus = usageStatus;
    }

    public Long getCouponTypeId() {
        return couponTypeId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public boolean getUsageStatus() {
        return usageStatus;
    }
}
