package cart.coupon.infrastructure.repository;

import static cart.coupon.exception.CouponExceptionType.NOT_FOUND_COUPON;

import cart.coupon.domain.Coupon;
import cart.coupon.domain.CouponRepository;
import cart.coupon.exception.CouponException;
import cart.coupon.infrastructure.dao.CouponDao;
import cart.coupon.infrastructure.mapper.CouponEntityMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class JdbcCouponRepository implements CouponRepository {

    private final CouponDao couponDao;

    public JdbcCouponRepository(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @Override
    public Long save(Coupon coupon) {
        return couponDao.save(CouponEntityMapper.toEntity(coupon));
    }

    @Override
    public Coupon findById(Long id) {
        return couponDao.findById(id)
                .map(CouponEntityMapper::toDomain)
                .orElseThrow(() -> new CouponException(NOT_FOUND_COUPON));
    }

    @Override
    public List<Coupon> findAllByMemberId(Long memberId) {
        return couponDao.findAllByMemberId(memberId).stream()
                .map(CouponEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Coupon> findAllByIds(final List<Long> ids) {
        return couponDao.findAllByIds(ids).stream()
                .map(CouponEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
