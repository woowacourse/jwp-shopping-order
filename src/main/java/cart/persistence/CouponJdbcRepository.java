package cart.persistence;

import cart.application.repository.CouponRepository;
import cart.domain.coupon.Coupon;
import cart.persistence.dao.CouponDao;
import cart.persistence.entity.CouponEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CouponJdbcRepository implements CouponRepository {

    private final CouponDao couponDao;

    public CouponJdbcRepository(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @Override
    public long create(final Coupon coupon) {
        CouponEntity entity = CouponEntity.from(coupon);
        return couponDao.create(entity);
    }

    @Override
    public Optional<Coupon> findById(final long id) {
        Optional<CouponEntity> optionalCoupon = couponDao.findById(id);
        if (optionalCoupon.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(optionalCoupon.get().toDomain());
    }

    @Override
    public List<Coupon> findAll() {
        List<CouponEntity> entities = couponDao.findAll();
        return entities.stream()
                .map(CouponEntity::toDomain)
                .collect(Collectors.toList());
    }
}
