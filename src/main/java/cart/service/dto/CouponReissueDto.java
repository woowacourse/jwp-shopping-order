package cart.service.dto;

import cart.controller.dto.CouponReissueRequest;

public class CouponReissueDto {

    private final long couponId;
    private final long memberId;


    public CouponReissueDto(final long couponId, final long memberId) {
        this.couponId = couponId;
        this.memberId = memberId;
    }

    public static CouponReissueDto of(final Long couponId, final CouponReissueRequest request) {
        return new CouponReissueDto(couponId, request.getId());
    }

    public long getCouponId() {
        return couponId;
    }

    public long getMemberId() {
        return memberId;
    }
}
