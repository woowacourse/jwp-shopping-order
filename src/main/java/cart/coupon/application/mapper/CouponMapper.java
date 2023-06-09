package cart.coupon.application.mapper;

import cart.coupon.application.dto.CouponResponse;
import cart.coupon.domain.Coupon;

public class CouponMapper {

  private CouponMapper() {
  }

  public static CouponResponse mapToCouponResponse(final Coupon coupon) {
    return new CouponResponse(
        coupon.getId(),
        coupon.getName(),
        coupon.findDiscountPrice().getValue()
    );
  }
}
