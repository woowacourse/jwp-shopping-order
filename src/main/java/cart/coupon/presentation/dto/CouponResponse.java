package cart.coupon.presentation.dto;

import cart.coupon.domain.Coupon;
import cart.coupon.domain.DiscountType;
import cart.coupon.domain.SpecificCouponType;
import cart.coupon.domain.TargetType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "targetType")
@JsonSubTypes({
        @Type(value = GeneralCouponResponse.class, name = "ALL"),
        @Type(value = SpecificCouponResponse.class, name = "SPECIFIC")
})
public abstract class CouponResponse {

    private Long id;
    private String name;
    private Long ownerMemberId;
    private DiscountType discountType;
    private TargetType targetType;
    private int value;


    public CouponResponse() {
    }

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
