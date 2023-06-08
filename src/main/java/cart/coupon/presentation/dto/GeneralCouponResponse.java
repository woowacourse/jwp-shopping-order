package cart.coupon.presentation.dto;

import cart.coupon.domain.DiscountType;
import cart.coupon.domain.TargetType;

public class GeneralCouponResponse extends CouponResponse {

    public GeneralCouponResponse(Long id,
                                 String name,
                                 Long ownerMemberId,
                                 DiscountType discountType,
                                 TargetType targetType,
                                 int value
    ) {
        super(id, name, ownerMemberId, discountType, targetType, value);
    }
}
