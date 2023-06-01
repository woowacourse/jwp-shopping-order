package cart.service.coupon;

import cart.controller.dto.CouponResponse;
import cart.controller.dto.CouponTypeResponse;
import cart.domain.coupon.Coupon;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {

    public CouponResponse toCouponResponse(final Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getName().getName(),
                coupon.getDiscountAmount().getDiscountAmount(),
                coupon.getDescription().getDescription(),
                coupon.getUsageStatus().getUsageStatus()
        );
    }

    public CouponTypeResponse toCouponTypeResponse(final Coupon coupon) {
        return new CouponTypeResponse(
                coupon.getId(),
                coupon.getName().getName(),
                coupon.getDiscountAmount().getDiscountAmount(),
                coupon.getDescription().getDescription());
    }
}
