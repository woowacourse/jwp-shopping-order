package cart.service;

import cart.domain.member.MemberCoupon;
import cart.dto.MemberCouponResponse;
import cart.repository.MemberCouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberCouponService {

    private final MemberCouponRepository memberCouponRepository;

    public MemberCouponService(final MemberCouponRepository memberCouponRepository) {
        this.memberCouponRepository = memberCouponRepository;
    }

    @Transactional(readOnly = true)
    public List<MemberCouponResponse> findAllByMemberId(final Long memberId) {
        final List<MemberCoupon> memberCoupons = memberCouponRepository.findAllByMemberIdWithUsed(memberId, false);
        return memberCoupons.stream()
                .map(MemberCouponResponse::from)
                .collect(Collectors.toList());
    }
}
