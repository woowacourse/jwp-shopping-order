package cart.application;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponType;
import cart.domain.coupon.MemberCoupon;
import cart.domain.member.Member;
import cart.domain.repository.CouponRepository;
import cart.domain.repository.MemberCouponRepository;
import cart.domain.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Long createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

//    private Coupon toCoupon(CouponDto couponDto) {
//        return new Coupon(
//                couponDto.getId(),
//                couponDto.getName(),
//                couponDto.getDiscountRate(),
//                couponDto.getPeriod(),
//                couponDto.getExpiredAt()
//        );
//    }

    @Transactional
    public Long issueCoupon(Long memberId, CouponType couponType) {
        Member member = memberRepository.findById(memberId);
        Coupon coupon = couponRepository.findByCouponType(couponType);

        MemberCoupon memberCoupon = MemberCoupon.issue(member, coupon);

        return memberCouponRepository.save(memberCoupon);
    }

    public List<MemberCoupon> getAllCouponsOfMember(Member member) {
        return memberCouponRepository.findAllByMemberId(member.getId());
    }
}
