package cart.dao.dto;

import cart.domain.MemberCoupon;

public class MemberCouponDto {

    private final Long id;
    private final Long ownerId;
    private final Long couponId;
    private final Boolean isUsed;

    public MemberCouponDto(Long id, Long ownerId, Long couponId, boolean isUsed) {
        this.id = id;
        this.ownerId = ownerId;
        this.couponId = couponId;
        this.isUsed = isUsed;
    }

    public static MemberCouponDto of(MemberCoupon memberCoupon) {
        return new MemberCouponDto(
                memberCoupon.getId(),
                memberCoupon.getOwner().getId(),
                memberCoupon.getCoupon().getId(),
                memberCoupon.isUsed()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }
}
