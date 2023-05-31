package cart.application;

import cart.application.dto.coupon.CouponRequest;
import cart.application.dto.coupon.CouponResponse;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponRepository;
import cart.exception.BadRequestException;
import cart.exception.ErrorCode;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(final CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public List<CouponResponse> getAllCoupons() {
        return couponRepository.findAll().stream()
            .map(couponWithId -> new CouponResponse(couponWithId.getCouponId(), couponWithId.getCoupon().name(),
                couponWithId.getCoupon().discountRate(), couponWithId.getCoupon().expiredAt()))
            .collect(Collectors.toUnmodifiableList());
    }

    public CouponResponse getCouponById(final Long id) {
        final Coupon coupon = couponRepository.findById(id);
        return new CouponResponse(id, coupon.name(), coupon.discountRate(), coupon.expiredAt());
    }

    @Transactional
    public long createCoupon(final CouponRequest couponRequest) {
        final String name = couponRequest.getName();
        final int discountRate = couponRequest.getDiscountRate();
        final int period = couponRequest.getPeriod();
        if (couponRepository.existByNameAndDiscountRate(name, discountRate)) {
            throw new BadRequestException(ErrorCode.COUPON_DUPLICATE);
        }
        final Coupon coupon = Coupon.create(name, discountRate, period);
        return couponRepository.insert(coupon);
    }

    @Transactional
    public void deleteCoupon(final Long couponId) {
        couponRepository.deleteById(couponId);
    }
}
