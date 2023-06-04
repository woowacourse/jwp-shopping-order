package cart.service;

import static java.util.stream.Collectors.toUnmodifiableList;

import cart.domain.VO.Money;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.DiscountPolicyType;
import cart.dto.coupon.CouponResponse;
import cart.dto.coupon.CouponSaveRequest;
import cart.repository.CouponRepository;
import cart.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;

    public CouponService(
            final CouponRepository couponRepository,
            final MemberRepository memberRepository
    ) {
        this.couponRepository = couponRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public List<CouponResponse> findAllByMemberId(final Long memberId) {
        return couponRepository.findAllByUsedAndMemberId(false, memberId).stream()
                .map(CouponResponse::of)
                .collect(toUnmodifiableList());
    }

    public void issuance(final CouponSaveRequest request) {
        final List<Coupon> coupons = memberRepository.findAll().stream()
                .map(member -> new Coupon(
                        request.getName(),
                        DiscountPolicyType.from(request.getType()),
                        request.getValue(),
                        Money.from(request.getMinimumPrice()),
                        member.getId()
                ))
                .collect(Collectors.toList());
        couponRepository.saveAll(coupons);
    }
}
