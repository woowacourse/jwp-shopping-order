package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.OrdersCouponDao;
import org.springframework.stereotype.Component;

@Component
public class CouponRepository {
    private final CouponDao couponDao;
    private final OrdersCouponDao ordersCouponDao;
    private final MemberCouponDao memberCouponDao;

    public CouponRepository(CouponDao couponDao, OrdersCouponDao ordersCouponDao, MemberCouponDao memberCouponDao) {
        this.couponDao = couponDao;
        this.ordersCouponDao = ordersCouponDao;
        this.memberCouponDao = memberCouponDao;
    }

    public void issueCoupon(final long memberId, final long couponId){
        memberCouponDao.create(memberId,couponId);
    }
}
