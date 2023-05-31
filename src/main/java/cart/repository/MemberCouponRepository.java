package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.domain.MemberCoupon;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponRepository {

    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;

    public MemberCouponRepository(CouponDao couponDao, MemberCouponDao memberCouponDao) {
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
    }

    public List<MemberCoupon> findAllByIds(Set<Long> ids) {
        return memberCouponDao.findAllByIds(ids);
    }
}
