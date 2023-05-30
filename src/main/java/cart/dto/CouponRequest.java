package cart.dto;

import cart.domain.Coupon;
import cart.domain.CouponType;

public class CouponRequest {

    private String name;
    private String type;
    private Integer discountAmount;

    public CouponRequest() {
    }

    public CouponRequest(String name, String type, Integer discountAmount) {
        this.name = name;
        this.type = type;
        this.discountAmount = discountAmount;
    }

    public Coupon toEntity() {
        return new Coupon(name, CouponType.from(type), discountAmount);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }
}
