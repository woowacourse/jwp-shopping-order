package cart.application;

import cart.dao.JdbcMemberCouponRepository;
import cart.domain.MemberCoupon;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberCouponService {

    private final JdbcMemberCouponRepository jdbcCouponRepository;

    public MemberCouponService(final JdbcMemberCouponRepository jdbcCouponRepository) {
        this.jdbcCouponRepository = jdbcCouponRepository;
    }

    public List<MemberCoupon> getMemberCoupons(final Long memberId) {
        return jdbcCouponRepository.findAllByMemberId(memberId);
    }
}
