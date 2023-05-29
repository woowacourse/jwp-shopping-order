package cart.repository;

import cart.dao.CouponDao;
import cart.dao.entity.CouponEntity;
import cart.domain.Member;
import org.springframework.stereotype.Repository;

@Repository
public class CouponJdbcRepository implements CouponRepository {

    private final CouponDao couponDao;

    public CouponJdbcRepository(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @Override
    public Long issue(final Member member, final Long couponId) {
        final CouponEntity couponEntity = toEntity(member, couponId);
        return couponDao.issue(couponEntity);
    }

    @Override
    public void changeStatus(final Long couponId, final Long memberId) {
        couponDao.changeStatus(couponId, memberId);
    }

    private CouponEntity toEntity(final Member member, final Long couponId) {
        return CouponEntity.of(member.getId(), couponId);
    }
}
