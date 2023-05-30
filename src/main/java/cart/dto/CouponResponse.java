package cart.dto;

import cart.domain.Coupon;

public class CouponResponse {

    private Long id;
    private String name;
    private String type;
    private Integer discountAmount;

    public CouponResponse() {
    }

    public CouponResponse(Long id, String name, String type, Integer discountAmount) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.discountAmount = discountAmount;
    }

    public static CouponResponse from(Coupon coupon) {
        return new CouponResponse(coupon.getId(), coupon.getType().name(), coupon.getName(),
                coupon.getDiscountAmount());
    }

    public Long getId() {
        return id;
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
