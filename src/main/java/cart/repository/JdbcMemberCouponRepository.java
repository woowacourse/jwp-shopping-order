package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.domain.Coupon;
import cart.domain.vo.Amount;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcMemberCouponRepository implements MemberCouponRepository {

    private final MemberCouponDao memberCouponDao;
    private final CouponDao couponDao;

    public JdbcMemberCouponRepository(final MemberCouponDao memberCouponDao, final CouponDao couponDao) {
        this.memberCouponDao = memberCouponDao;
        this.couponDao = couponDao;
    }

    @Override
    public Coupon findByCouponIdAndMemberId(final Long couponId, final Long memberId) {
        final MemberCouponEntity memberCouponEntity = memberCouponDao.findByCouponIdAndMemberId(couponId, memberId)
                .orElseThrow(NoSuchElementException::new);
        final CouponEntity couponEntity = couponDao.findById(couponId)
                .orElseThrow(NoSuchElementException::new);
        return new Coupon(couponId, couponEntity.getName(), Amount.of(couponEntity.getDiscountAmount()),
                Amount.of(couponEntity.getMinAmount()), memberCouponEntity.isUsed());
    }

    @Override
    public void update(final Coupon coupon, final Long memberId) {
        memberCouponDao.update(coupon, memberId);
        couponDao.update(coupon);
    }
}
