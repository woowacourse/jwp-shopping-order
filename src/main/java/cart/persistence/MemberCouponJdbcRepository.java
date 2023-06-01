package cart.persistence;

import cart.application.repository.MemberCouponRepository;
import cart.domain.Member;
import cart.domain.coupon.MemberCoupon;
import cart.persistence.dao.MemberCouponDao;
import cart.persistence.dto.MemberCouponDetailDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponJdbcRepository implements MemberCouponRepository {

    private final MemberCouponDao memberCouponDao;

    public MemberCouponJdbcRepository(final MemberCouponDao memberCouponDao) {
        this.memberCouponDao = memberCouponDao;
    }

    @Override
    public List<MemberCoupon> findValidCouponsByMember(final Member member) {
        List<MemberCouponDetailDTO> memberCouponDetails = memberCouponDao.findValidCouponByMemberId(member.getId());
        return memberCouponDetails.stream()
                .map(MemberCouponDetailDTO::toDomain)
                .collect(Collectors.toList());
    }
}
