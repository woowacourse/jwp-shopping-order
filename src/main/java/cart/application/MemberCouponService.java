package cart.application;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponRepository;
import cart.domain.memberCoupon.MemberCoupon;
import cart.domain.memberCoupon.MemberCouponRepository;
import cart.dto.MemberCouponRequest;
import cart.dto.MemberCouponResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberCouponService {
    private final MemberCouponRepository memberCouponRepository;
    private final CouponRepository couponRepository;

    public MemberCouponService(MemberCouponRepository memberCouponRepository, CouponRepository couponRepository) {
        this.memberCouponRepository = memberCouponRepository;
        this.couponRepository = couponRepository;
    }

    public long add(Member member, MemberCouponRequest memberCouponRequest) {
        Coupon coupon = couponRepository.findById(memberCouponRequest.getId());
        return memberCouponRepository.add(new MemberCoupon(member, coupon));
    }

    public MemberCouponResponse findById(Long id) { // TODO model to dto 개선 필요
        MemberCoupon memberCoupon = memberCouponRepository.findById(id);
        return new MemberCouponResponse(
                memberCoupon.getId(),
                memberCoupon.getCoupon().getName(),
                memberCoupon.getCoupon().getDiscountType().getName(),
                memberCoupon.getCoupon().getDiscountPercent(),
                memberCoupon.getCoupon().getDiscountAmount(),
                memberCoupon.getCoupon().getMinimumPrice()
        );
    }

    public List<MemberCouponResponse> findMemberCouponsByMemberId(Long memberId) {
        List<MemberCoupon> memberCoupons = memberCouponRepository.findMemberCouponsByMemberId(memberId);
        return memberCoupons.stream()
                .map(memberCoupon -> new MemberCouponResponse(
                                memberCoupon.getId(),
                                memberCoupon.getCoupon().getName(),
                                memberCoupon.getCoupon().getDiscountType().getName(),
                                memberCoupon.getCoupon().getDiscountPercent(),
                                memberCoupon.getCoupon().getDiscountAmount(),
                                memberCoupon.getCoupon().getMinimumPrice()
                        )
                )
                .collect(Collectors.toList());
    }

    public List<MemberCouponResponse> findAll() {
        List<MemberCoupon> memberCoupons = memberCouponRepository.findAll();
        return memberCoupons.stream()
                .map(memberCoupon -> new MemberCouponResponse(
                                memberCoupon.getId(),
                                memberCoupon.getCoupon().getName(),
                                memberCoupon.getCoupon().getDiscountType().getName(),
                                memberCoupon.getCoupon().getDiscountPercent(),
                                memberCoupon.getCoupon().getDiscountAmount(),
                                memberCoupon.getCoupon().getMinimumPrice()
                        )
                )
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        memberCouponRepository.delete(id);
    }
}
