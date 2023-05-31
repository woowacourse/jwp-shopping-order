package cart.coupon.infrastructure.entity;

import cart.coupon.domain.DiscountType;
import cart.coupon.domain.TargetType;

public class CouponEntity {

    private Long id;
    private String name;
    private Long memberId;
    private DiscountType discountType;
    private TargetType targetType;
    private Long targetProductId;
    private int couponValue;

    CouponEntity() {
    }

    public CouponEntity(
            Long id,
            String name,
            Long memberId,
            DiscountType discountType,
            TargetType targetType,
            Long targetProductId,
            int couponValue
    ) {
        this.id = id;
        this.name = name;
        this.memberId = memberId;
        this.discountType = discountType;
        this.targetType = targetType;
        this.targetProductId = targetProductId;
        this.couponValue = couponValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(TargetType targetType) {
        this.targetType = targetType;
    }

    public Long getTargetProductId() {
        return targetProductId;
    }

    public void setTargetProductId(Long targetProductId) {
        this.targetProductId = targetProductId;
    }

    public int getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(int couponValue) {
        this.couponValue = couponValue;
    }
}
