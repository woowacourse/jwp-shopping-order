package cart.service;

import cart.domain.member.MemberCoupon;
import cart.repository.MemberCouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberCouponService {

    private final MemberCouponRepository memberCouponRepository;

    public MemberCouponService(final MemberCouponRepository memberCouponRepository) {
        this.memberCouponRepository = memberCouponRepository;
    }

    @Transactional(readOnly = true)
    public List<MemberCoupon> findAllByMemberId(final Long memberId) {
        return memberCouponRepository.findAllByMemberIdWithUsed(memberId, false);
    }
}
