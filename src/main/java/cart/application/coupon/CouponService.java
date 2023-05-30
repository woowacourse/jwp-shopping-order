package cart.application.coupon;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.dto.CouponIssueRequest;
import cart.dto.CouponReissueRequest;
import cart.exception.CannotChangeCouponStatusException;
import cart.exception.CannotDeleteCouponException;
import cart.repository.MemberJdbcRepository;
import cart.repository.MemberRepository;
import cart.repository.coupon.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CouponService {

    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;

    public CouponService(final CouponRepository couponRepository, final MemberJdbcRepository memberRepository) {
        this.couponRepository = couponRepository;
        this.memberRepository = memberRepository;
    }

    public Long issueCoupon(final Member member, final CouponIssueRequest request) {
        return couponRepository.issue(member, request.getId());
    }

    public void reissueCoupon(final Long couponId, final CouponReissueRequest request) {
        final Member member = memberRepository.findMemberByMemberIdWithCoupons(request.getId());
        final Coupon coupon = member.findCoupon(couponId);

        if (coupon.isNotUsed()) {
            throw new CannotChangeCouponStatusException();
        }

        couponRepository.changeStatus(couponId, member.getId());
    }

    public void deleteCoupon(final Long couponId, final Long memberId) {
        final Coupon coupon = couponRepository.findCouponByCouponIdAndMemberId(couponId, memberId);

        if (coupon.isNotUsed()) {
            throw new CannotDeleteCouponException();
        }

        couponRepository.deleteCoupon(coupon.getId());
    }
}
