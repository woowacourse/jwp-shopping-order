package shop.application.coupon;

import shop.domain.coupon.Coupon;
import shop.domain.coupon.CouponType;
import shop.domain.coupon.MemberCoupon;
import shop.domain.member.Member;
import shop.domain.repository.CouponRepository;
import shop.domain.repository.MemberCouponRepository;
import shop.domain.repository.MemberRepository;
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

//    @Transactional
//    public Long createCoupon(Coupon coupon) {
//        return couponRepository.save(coupon);
//    }

//    private Coupon toCoupon(CouponDto couponDto) {
//        return new Coupon(
//                couponDto.getId(),
//                couponDto.getName(),
//                couponDto.getDiscountRate(),
//                couponDto.getPeriod(),
//                couponDto.getExpiredAt()
//        );
//    }

    // TODO: 2023-05-31 couponType 어떻게 하지 ?
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
