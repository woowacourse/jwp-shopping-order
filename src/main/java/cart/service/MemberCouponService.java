package cart.service;

import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.dto.CouponResponse;
import cart.repository.MemberCouponRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberCouponService {

    private final MemberCouponRepository memberCouponRepository;

    public MemberCouponService(MemberCouponRepository memberCouponRepository) {
        this.memberCouponRepository = memberCouponRepository;
    }

    public CouponResponse findAll(Member member) {
        List<MemberCoupon> memberCoupons = memberCouponRepository.findNotExpiredAllByMember(member);
        return CouponResponse.from(memberCoupons);
    }
}
