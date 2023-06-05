package cart.service.coupon;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponRepository;
import cart.domain.member.Member;
import cart.domain.member.MemberRepository;
import cart.exception.CannotChangeCouponStatusException;
import cart.exception.CannotDeleteCouponException;
import cart.repository.MemberJdbcRepository;
import cart.service.dto.CouponReissueDto;
import cart.service.dto.CouponSaveDto;
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

    public Long issueCoupon(final CouponSaveDto saveDto) {
        final Member member = new Member(saveDto.getId(), saveDto.getEmail(), saveDto.getPassword());
        return couponRepository.issue(member, saveDto.getCouponId());
    }

    public void reissueCoupon(final CouponReissueDto reissueDto) {
        final Member member = memberRepository.findMemberByMemberIdWithCoupons(reissueDto.getMemberId());
        final Coupon coupon = member.findCoupon(reissueDto.getCouponId());

        if (coupon.isNotUsed()) {
            throw new CannotChangeCouponStatusException();
        }

        couponRepository.changeStatusTo(reissueDto.getCouponId(), Boolean.FALSE);
    }

    public void deleteCoupon(final Long couponId) {
        final Coupon coupon = couponRepository.findCouponById(couponId);

        if (coupon.isNotUsed()) {
            throw new CannotDeleteCouponException();
        }

        couponRepository.deleteCoupon(coupon.getId());
    }
}
