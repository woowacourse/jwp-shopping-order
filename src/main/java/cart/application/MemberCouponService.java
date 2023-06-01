package cart.application;

import cart.dao.CouponRepository;
import cart.dao.MemberCouponRepository;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.dto.coupon.MemberCouponResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberCouponService {

    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;

    public MemberCouponService(final CouponRepository couponRepository, final MemberCouponRepository memberCouponRepository) {
        this.couponRepository = couponRepository;
        this.memberCouponRepository = memberCouponRepository;
    }

    public void add(final Long memberId, final Long couponId) {
        Coupon coupon = couponRepository.findById(couponId);
        memberCouponRepository.save(memberId, new MemberCoupon(coupon));
    }

    public List<MemberCouponResponse> getMemberCoupons(final Long memberId) {
        List<MemberCoupon> memberCoupons = memberCouponRepository.findAllByMemberId(memberId);
        return memberCoupons.stream()
                .map(MemberCouponResponse::from)
                .collect(Collectors.toList());
    }
}
