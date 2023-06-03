package cart.persistence.repository;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponRepository;
import cart.exception.NoSuchCouponException;
import cart.persistence.dao.CouponDao;
import cart.persistence.entity.CouponEntity;
import cart.persistence.mapper.CouponMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DbCouponRepository implements CouponRepository {
    private final CouponDao couponDao;

    public DbCouponRepository(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @Override
    public List<Coupon> findAll() {
        List<CouponEntity> couponEntities = couponDao.findAll();
        return couponEntities.stream()
                .map(CouponMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Coupon findById(Long id) {
        CouponEntity couponEntity = couponDao.findById(id).orElseThrow(() -> new NoSuchCouponException());
        return CouponMapper.toDomain(couponEntity);
    }

    @Override
    public Long add(Coupon coupon) {
        return couponDao.add(CouponMapper.toEntity(coupon));
    }

    @Override
    public void delete(Long id) {
        couponDao.delete(id);
    }
}
