package cart.repository.coupon;

import cart.dao.coupon.MemberCouponDao;
import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponRepository {

    private final MemberCouponDao memberCouponDao;

    public MemberCouponRepository(final MemberCouponDao memberCouponDao) {
        this.memberCouponDao = memberCouponDao;
    }

    public long save(final Member member, final Coupon giftCoupon) {
        return memberCouponDao.save(member.getId(), giftCoupon.getId());
    }
}
