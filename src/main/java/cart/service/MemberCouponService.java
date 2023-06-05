package cart.service;

import cart.domain.coupon.MemberCoupon;
import cart.dto.MemberCouponDto;
import cart.repository.MemberCouponRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MemberCouponService {

    private final MemberCouponRepository memberCouponRepository;

    public MemberCouponService(final MemberCouponRepository memberCouponRepository) {
        this.memberCouponRepository = memberCouponRepository;
    }

    @Transactional(readOnly = true)
    public List<MemberCouponDto> findByMemberId(final Long memberId) {
        final List<MemberCoupon> findMemberCoupons = memberCouponRepository.findAllByMemberId(memberId);

        return findMemberCoupons.stream()
                .map(this::getMemberCouponDto)
                .collect(Collectors.toList());
    }

    private MemberCouponDto getMemberCouponDto(final MemberCoupon memberCoupon) {
        return new MemberCouponDto(
                memberCoupon.getId(),
                memberCoupon.getCouponName(),
                memberCoupon.getCouponType(),
                memberCoupon.getDiscountValue(),
                memberCoupon.getMinimumPrice()
        );
    }
}
