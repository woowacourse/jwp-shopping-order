package cart.persistence.coupon;

import java.math.BigDecimal;

import cart.domain.coupon.type.CouponInfo;

public class MemberJoinCouponInfo {

	private Long memberId;
	private CouponInfo couponInfo;

	public MemberJoinCouponInfo(final Long memberId, final Long couponId, final String couponName,
		final String discountType, final BigDecimal discount) {
		this.memberId = memberId;
		this.couponInfo = CouponInfo.of(couponId, discountType, couponName, discount);
	}

	public Long getMemberId() {
		return memberId;
	}

	public CouponInfo getCouponInfo() {
		return couponInfo;
	}
}
