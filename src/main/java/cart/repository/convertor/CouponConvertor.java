package cart.repository.convertor;

import cart.domain.Coupon;
import cart.dto.CouponDto;

public class CouponConvertor {

    private CouponConvertor() {
    }

    public static Coupon dtoToDomain(CouponDto couponDto) {
        return new Coupon(
                couponDto.getId(),
                couponDto.getName(),
                couponDto.getDiscountRate(),
                couponDto.getDiscountPrice()
        );
    }

}
