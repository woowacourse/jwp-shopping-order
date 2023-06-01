package cart.coupon.presentation.dto;

import cart.coupon.domain.Coupon;
import cart.coupon.domain.DiscountType;
import cart.coupon.domain.SpecificCouponType;
import cart.coupon.domain.TargetType;

public abstract class CouponResponse {

    private final Long id;
    private final String name;
    private final Long ownerMemberId;
    private final DiscountType discountType;
    private final TargetType targetType;
    private final int value;

    protected CouponResponse(
            Long id,
            String name,
            Long ownerMemberId,
            DiscountType discountType,
            TargetType targetType,
            int value
    ) {
        this.id = id;
        this.name = name;
        this.ownerMemberId = ownerMemberId;
        this.discountType = discountType;
        this.targetType = targetType;
        this.value = value;
    }

    public static CouponResponse from(Coupon coupon) {
        if (coupon.targetType() == TargetType.ALL) {
            return new GeneralCouponResponse(coupon.getId(),
                    coupon.getName(),
                    coupon.getMemberId(),
                    coupon.discountType(),
                    coupon.targetType(),
                    coupon.value()
            );
        }
        SpecificCouponType couponType = (SpecificCouponType) coupon.getCouponType();
        return new SpecificCouponResponse(
                coupon.getId(),
                coupon.getName(),
                coupon.getMemberId(),
                coupon.discountType(),
                coupon.targetType(),
                coupon.value(),
                couponType.getProductId()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getOwnerMemberId() {
        return ownerMemberId;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public int getValue() {
        return value;
    }
}
