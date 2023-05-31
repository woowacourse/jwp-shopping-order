package cart.application;

import cart.dao.CouponDao;
import cart.domain.Coupon;
import cart.domain.Discount;
import cart.dto.coupon.CouponRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {

    private final CouponDao couponDao;

    public CouponService(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    public List<Coupon> findAll() {
        List<Coupon> coupons = couponDao.findAll();
        return coupons;
    }

    public Coupon findById(Long id) {
        Coupon coupon = couponDao.findById(id);
        return coupon;
    }

    public Long create(CouponRequest request) {
        Coupon coupon = new Coupon(request.getName(), new Discount(request.getType(), request.getAmount()));
        return couponDao.create(coupon);
    }

    public void delete(Long id) {
        couponDao.delete(id);
    }
}
