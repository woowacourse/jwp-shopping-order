package cart.service;

import cart.domain.Member;
import cart.domain.Money;
import cart.domain.coupon.MemberCoupon;
import cart.domain.coupon.repository.MemberCouponRepository;
import cart.domain.repository.MemberRepository;
import cart.dto.AuthMember;
import cart.dto.MemberCouponsResponse;
import cart.exception.ExceptionType;
import cart.exception.MemberException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final MemberCouponRepository memberCouponRepository;
    private final MemberRepository memberRepository;
    private final CouponIssuer couponIssuer;

    public CouponService(
            MemberCouponRepository memberCouponRepository,
            MemberRepository memberRepository,
            CouponIssuer couponIssuer
    ) {
        this.memberCouponRepository = memberCouponRepository;
        this.memberRepository = memberRepository;
        this.couponIssuer = couponIssuer;
    }

    public MemberCouponsResponse findAll(AuthMember authMember) {
        Member member = toMember(authMember);
        List<MemberCoupon> memberCoupons = memberCouponRepository.findNotExpired(member);
        return MemberCouponsResponse.from(memberCoupons);
    }

    private Member toMember(AuthMember authMember) {
        return memberRepository.findById(authMember.getId())
                .orElseThrow(() -> new MemberException(ExceptionType.NOT_FOUND_MEMBER));
    }

    public void issueByOrderPrice(Money totalOrderPrice, Member member) {
        List<MemberCoupon> memberCoupons = couponIssuer.issueAllCoupons(totalOrderPrice).stream()
                .map(it -> new MemberCoupon(member, it))
                .collect(Collectors.toList());
        if (!memberCoupons.isEmpty()) {
            memberCouponRepository.saveAll(memberCoupons);
        }
    }
}
