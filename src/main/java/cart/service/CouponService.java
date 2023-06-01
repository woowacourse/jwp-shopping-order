package cart.service;

import static java.util.stream.Collectors.toUnmodifiableList;

import cart.domain.Member;
import cart.domain.coupon.MemberCoupon;
import cart.repository.CouponRepository;
import cart.repository.MemberCouponRepository;
import cart.service.response.CouponResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final MemberCouponRepository memberCouponRepository;
    private final CouponRepository couponRepository;

    public CouponService(final MemberCouponRepository memberCouponRepository, final CouponRepository couponRepository) {
        this.memberCouponRepository = memberCouponRepository;
        this.couponRepository = couponRepository;
    }

    public List<CouponResponse> findMemberCoupons(final Member member) {
        final List<MemberCoupon> memberCoupons = memberCouponRepository.findByMemberId(member.getId());
        return memberCoupons.stream()
                .map(memberCoupon -> new CouponResponse(memberCoupon.getId(), memberCoupon.getCoupon().getName()))
                .collect(toUnmodifiableList());
    }
}
