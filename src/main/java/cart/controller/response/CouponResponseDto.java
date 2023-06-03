package cart.controller.response;

import cart.domain.MemberCoupon;

public class CouponResponseDto {

    private final Long memberCouponId;
    private final String name;

    private CouponResponseDto(final Long memberCouponId,
                              final String name) {
        this.memberCouponId = memberCouponId;
        this.name = name;
    }

    public static CouponResponseDto from(MemberCoupon memberCoupon) {
        String couponName = memberCoupon.getCoupon()
                .getName();
        return new CouponResponseDto(memberCoupon.getId(), couponName);
    }

    public Long getMemberCouponId() {
        return memberCouponId;
    }

    public String getName() {
        return name;
    }

}
