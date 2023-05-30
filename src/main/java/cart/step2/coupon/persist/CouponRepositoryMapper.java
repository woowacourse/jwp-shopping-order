package cart.step2.coupon.persist;

import cart.step2.coupon.domain.Coupon;
import cart.step2.coupon.domain.CouponEntity;
import cart.step2.coupon.domain.repository.CouponRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CouponRepositoryMapper implements CouponRepository {

    private final CouponDao couponDao;

    public CouponRepositoryMapper(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @Override
    public void updateUsageStatus(final Long memberId, final Long couponId) {
        couponDao.updateUsageStatus(memberId, couponId);
    }

    @Override
    public Long create(final Long memberId, final Long couponTypeId) {
        return couponDao.create(memberId, couponTypeId);
    }

    @Override
    public List<Coupon> findAll(final Long memberId) {
        return couponDao.findAll(memberId).stream()
                .map(CouponEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(final Long couponId) {
        couponDao.deleteById(couponId);
    }

    @Override
    public Coupon findById(final Long couponId) {
        return couponDao.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("쿠폰을 찾을 수 없습니다. 쿠폰 ID가 일치하는지 확인해주세요."))
                .toDomain();
    }

}
