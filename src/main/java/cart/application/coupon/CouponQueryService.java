package cart.application.coupon;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.coupon.dto.CouponResponse;
import cart.domain.coupon.MemberCoupon;
import cart.domain.coupon.MemberCouponRepository;

@Transactional(readOnly = true)
@Service
public class CouponQueryService {

	private final MemberCouponRepository memberCouponRepository;

	public CouponQueryService(final MemberCouponRepository memberCouponRepository) {
		this.memberCouponRepository = memberCouponRepository;
	}

	public List<CouponResponse> findByMemberId(final Long memberId) {
		final MemberCoupon memberCoupon = memberCouponRepository.findByMemberId(memberId);
		return memberCoupon.getCouponInfos().stream()
			.map(couponInfo -> new CouponResponse(couponInfo.getId(), couponInfo.getName(), couponInfo.getDiscount()))
			.collect(Collectors.toList());
	}
}
