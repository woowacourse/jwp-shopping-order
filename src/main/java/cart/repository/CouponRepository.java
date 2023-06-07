package cart.repository;

import cart.dao.CouponDao;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.Discount;
import cart.domain.coupon.StrategyFactory;
import cart.entity.CouponEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CouponRepository {

    private final CouponDao couponDao;
    private final StrategyFactory strategyFactory;

    public CouponRepository(final CouponDao couponDao, final StrategyFactory strategyFactory) {
        this.couponDao = couponDao;
        this.strategyFactory = strategyFactory;
    }

    public List<Coupon> findAll() {
        List<CouponEntity> couponEntities = couponDao.findAll();
        return couponEntities.stream()
                .map(this::toMemberCoupon)
                .collect(Collectors.toList());
    }

    private Coupon toMemberCoupon(final CouponEntity entity) {
        return new Coupon(
                entity.getId(),
                entity.getName(),
                new Discount(
                        strategyFactory.findStrategy(entity.getDiscountType()),
                        entity.getAmount()
                )
        );
    }

    public Long create(Coupon coupon) {
        return couponDao.create(CouponEntity.from(coupon));
    }

    public void delete(Long id) {
        couponDao.delete(id);
    }
}
