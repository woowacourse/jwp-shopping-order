package cart.application.coupon;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.coupon.dto.AdminCouponResponse;
import cart.application.coupon.dto.CouponDetailResponse;
import cart.application.coupon.dto.CouponResponse;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponRepository;
import cart.domain.coupon.MemberCoupon;
import cart.domain.coupon.MemberCouponRepository;
import cart.domain.coupon.type.CouponInfo;

@Transactional(readOnly = true)
@Service
public class CouponQueryService {

	private final CouponRepository couponRepository;
	private final MemberCouponRepository memberCouponRepository;

	public CouponQueryService(final CouponRepository couponRepository,
		final MemberCouponRepository memberCouponRepository) {
		this.couponRepository = couponRepository;
		this.memberCouponRepository = memberCouponRepository;
	}

	public List<AdminCouponResponse> findAll() {
		final List<Coupon> coupons = couponRepository.findAll();
		return coupons.stream()
			.map(AdminCouponResponse::from)
			.collect(Collectors.toList());
	}

	public List<CouponResponse> findByMemberId(final Long memberId) {
		final MemberCoupon memberCoupon = memberCouponRepository.findByMemberId(memberId);
		return convertToCouponResponses(memberCoupon.getCouponInfos());
	}

	public CouponDetailResponse findCouponById(final Long couponId) {
		Coupon coupon = couponRepository.findCouponById(couponId);
		return CouponDetailResponse.from(coupon);
	}

	private List<CouponResponse> convertToCouponResponses(final List<CouponInfo> couponInfos) {
		return couponInfos.stream()
			.map(CouponResponse::new)
			.collect(Collectors.toList());
	}
}
