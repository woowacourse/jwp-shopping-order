package cart.application;

import cart.domain.coupon.Coupon;
import cart.dto.coupon.CouponRequest;
import cart.repository.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(final CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public List<Coupon> findAll() {
        List<Coupon> coupons = couponRepository.findAll();
        return coupons;
    }

    public Long create(CouponRequest request) {
        return couponRepository.create(request.toCoupon());
    }

    public void delete(Long id) {
        couponRepository.delete(id);
    }
}
