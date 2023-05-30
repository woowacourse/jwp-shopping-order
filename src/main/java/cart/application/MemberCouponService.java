package cart.application;

import cart.dao.CouponRepository;
import cart.dao.MemberCouponRepository;
import cart.domain.Coupon;
import cart.domain.MemberCoupon;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberCouponService {

    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;

    public MemberCouponService(final CouponRepository couponRepository, final MemberCouponRepository memberCouponRepository) {
        this.couponRepository = couponRepository;
        this.memberCouponRepository = memberCouponRepository;
    }

    public List<MemberCoupon> getMemberCoupons(final Long memberId) {
        return memberCouponRepository.findAllByMemberId(memberId);
    }

    public void add(final Long memberId, final Long couponId) {
        Coupon coupon = couponRepository.findById(couponId);
        memberCouponRepository.save(memberId, new MemberCoupon(coupon));
    }
}
