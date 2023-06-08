package cart.repository.convertor;

import cart.dao.dto.CouponDto;
import cart.domain.Coupon;

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
