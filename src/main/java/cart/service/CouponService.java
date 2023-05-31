package cart.service;

import static java.util.stream.Collectors.toUnmodifiableList;

import cart.domain.cart.MemberCoupon;
import cart.domain.coupon.Coupon;
import cart.dto.CouponResponse;
import cart.dto.CouponSaveRequest;
import cart.repository.CouponRepository;
import cart.repository.MemberCouponRepository;
import cart.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CouponService {

    private final MemberCouponRepository memberCouponRepository;
    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;

    public CouponService(final MemberCouponRepository memberCouponRepository,
                         final CouponRepository couponRepository,
                         final MemberRepository memberRepository) {
        this.memberCouponRepository = memberCouponRepository;
        this.couponRepository = couponRepository;
        this.memberRepository = memberRepository;
    }

    public List<CouponResponse> findAllByMemberId(final Long memberId) {
        return memberCouponRepository.findAllByMemberId(memberId).stream()
                .map(CouponResponse::from)
                .collect(toUnmodifiableList());
    }

    public Long issuance(final CouponSaveRequest couponSaveRequest) {
        final Coupon coupon = couponSaveRequest.toDomain();
        final Coupon savedCoupon = couponRepository.save(coupon);

        final List<MemberCoupon> memberCoupons = memberRepository.findAll().stream()
                .map(member -> new MemberCoupon(member.getId(), savedCoupon))
                .collect(Collectors.toList());
        memberCouponRepository.saveAll(memberCoupons);
        return savedCoupon.getId();
    }
}
