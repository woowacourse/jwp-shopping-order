package shop.application.coupon;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.application.member.dto.MemberCouponDto;
import shop.domain.coupon.Coupon;
import shop.domain.coupon.CouponType;
import shop.domain.coupon.MemberCoupon;
import shop.domain.member.Member;
import shop.domain.repository.CouponRepository;
import shop.domain.repository.MemberCouponRepository;
import shop.domain.repository.MemberRepository;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class CouponService {
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;

    public CouponService(MemberRepository memberRepository, CouponRepository couponRepository,
                         MemberCouponRepository memberCouponRepository) {
        this.memberRepository = memberRepository;
        this.couponRepository = couponRepository;
        this.memberCouponRepository = memberCouponRepository;
    }

    @Transactional
    public Long issueCoupon(Long memberId, CouponType couponType) {
        Member member = memberRepository.findById(memberId);
        Coupon coupon = couponRepository.findByCouponType(couponType);

        MemberCoupon memberCoupon = MemberCoupon.issue(member, coupon);

        return memberCouponRepository.save(memberCoupon);
    }

    public List<MemberCouponDto> getAllCouponsOfMember(Member member) {
        List<MemberCoupon> memberCoupons = memberCouponRepository.findAllByMemberId(member.getId());

        return MemberCouponDto.of(memberCoupons);
    }
}
