package cart.infrastructure.repository;

import cart.domain.Coupon;
import cart.domain.repository.CouponRepository;
import cart.entity.CouponEntity;
import cart.infrastructure.dao.CouponDao;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcCouponRepository implements CouponRepository {

    private final CouponDao couponDao;

    public JdbcCouponRepository(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @Override
    public List<Coupon> findAll() {
        return couponDao.findAll().stream()
                .map(CouponEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Coupon findById(final Long id) {
        return couponDao.findById(id)
                .orElseThrow(NoSuchElementException::new)
                .toDomain();
    }
}
