package cart.service;

import cart.dao.CouponDao;
import cart.domain.coupon.Coupon;
import cart.dto.CouponResponse;
import cart.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(final CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public List<CouponResponse> findAll() {
        final List<Coupon> coupons = couponRepository.findAll();
        return coupons.stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());
    }
}
