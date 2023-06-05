package cart.application;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.domain.Coupon;
import cart.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;

    public CouponService(CouponDao couponDao, MemberCouponDao memberCouponDao) {
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
    }

    public Coupon findById(long couponId) {
        return couponDao.findById(couponId);
    }

    public void issueCouponByIdToMember(long couponId, Member member) {
        memberCouponDao.save(member.getId(), couponId);
    }
}
