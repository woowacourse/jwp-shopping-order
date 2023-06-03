package cart.infrastructure.repository;

import cart.domain.MemberCoupon;
import cart.domain.repository.MemberCouponRepository;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import cart.infrastructure.dao.CouponDao;
import cart.infrastructure.dao.MemberCouponDao;
import java.util.List;
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
    public MemberCoupon findByCouponIdAndMemberId(final Long couponId, final Long memberId) {
        final MemberCouponEntity memberCouponEntity = memberCouponDao.findByCouponIdAndMemberId(couponId, memberId)
                .orElseThrow(NoSuchElementException::new);
        final CouponEntity couponEntity = couponDao.findById(couponId)
                .orElseThrow(NoSuchElementException::new);
        return new MemberCoupon(memberCouponEntity.getId(), memberId, couponEntity.toDomain(),
                memberCouponEntity.isUsed());
    }

    @Override
    public List<Long> findCouponIdsByMemberId(final Long memberId) {
        return memberCouponDao.findCouponIdsByMemberId(memberId);
    }

    @Override
    public void update(final MemberCoupon coupon, final Long memberId) {
        memberCouponDao.update(MemberCouponEntity.from(coupon));
        couponDao.update(CouponEntity.from(coupon.getCoupon()));
    }
}
