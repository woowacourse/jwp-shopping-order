package cart.repository;

import static java.util.stream.Collectors.toUnmodifiableList;

import cart.dao.CouponDao;
import cart.dao.dto.CouponDto;
import cart.domain.coupon.Coupon;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CouponRepository {

    private final CouponDao couponDao;

    public CouponRepository(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    public Coupon insert(final Coupon coupon) {
        return couponDao.insert(CouponDto.from(coupon)).toCoupon();
    }

    public Coupon findById(final Long id) {
        return couponDao.findById(id).toCoupon();
    }

    public List<Coupon> findByIds(final Collection<Long> ids) {
        return couponDao.findByIds(ids).stream()
                .map(CouponDto::toCoupon)
                .collect(toUnmodifiableList());
    }

    public List<Coupon> findByMemberId(final Long id) {
        return couponDao.findByMemberId(id).stream()
                .map(CouponDto::toCoupon)
                .collect(toUnmodifiableList());
    }
}
