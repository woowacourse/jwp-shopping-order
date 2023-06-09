package cart.domain.coupon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import cart.domain.coupon.type.CouponInfo;

public class MemberCoupon {

	private final Long memberId;
	private final Map<Long, CouponInfo> couponInfos;

	public MemberCoupon(final Long memberId, final List<CouponInfo> couponInfos) {
		this.memberId = memberId;
		this.couponInfos = couponInfoToMapById(couponInfos);
	}

	public boolean contain(final Long couponInfoId) {
		return couponInfos.containsKey(couponInfoId);
	}

	public Optional<CouponInfo> getCouponInfo(final Long couponInfoId) {
		return Optional.ofNullable(couponInfos.get(couponInfoId));
	}

	public Long getMemberId() {
		return memberId;
	}

	public List<CouponInfo> getCouponInfos() {
		return new ArrayList<>(couponInfos.values());
	}

	private static Map<Long, CouponInfo> couponInfoToMapById(final List<CouponInfo> couponInfos) {
		return couponInfos.stream()
			.collect(Collectors.toMap(CouponInfo::getId, Function.identity()));
	}
}
