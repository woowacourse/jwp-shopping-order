package cart.coupon.presentation.dto;

import cart.coupon.domain.DiscountType;
import cart.coupon.domain.TargetType;

public class SpecificCouponResponse extends CouponResponse{

    private final Long targetProductId;

    public SpecificCouponResponse(
            Long id,
            String name,
            Long ownerMemberId,
            DiscountType discountType,
            TargetType targetType,
            int value,
            Long targetProductId
    ) {
        super(id, name, ownerMemberId, discountType, targetType, value);
        this.targetProductId = targetProductId;
    }

    public Long getTargetProductId() {
        return targetProductId;
    }
}
