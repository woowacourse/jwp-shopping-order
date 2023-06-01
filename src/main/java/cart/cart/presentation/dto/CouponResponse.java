package cart.cart.presentation.dto;

import cart.coupon.Coupon;

public class CouponResponse {
    private long id;
    private String name;

    public CouponResponse(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CouponResponse from(Coupon coupon) {
        return new CouponResponse(coupon.getId(), coupon.getName());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
