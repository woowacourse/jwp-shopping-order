package cart.application;

import static java.util.stream.Collectors.toList;

import cart.domain.MemberCoupon;
import cart.dto.response.MemberCouponResponse;
import cart.repository.MemberCouponRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberCouponService {

    private final MemberCouponRepository memberCouponRepository;

    public MemberCouponService(MemberCouponRepository memberCouponRepository) {
        this.memberCouponRepository = memberCouponRepository;
    }

    @Transactional(readOnly = true)
    public List<MemberCouponResponse> findAllByMemberId(Long memberId) {
        List<MemberCoupon> memberCoupons = memberCouponRepository.findAllByMemberId(memberId);
        return memberCoupons.stream()
                .map(MemberCouponResponse::from)
                .collect(toList());
    }
}
