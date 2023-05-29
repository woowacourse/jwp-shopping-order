package cart.dto;

import cart.domain.Coupon;

public class CouponResponse {
    private final Long id;
    private final String name;

    public CouponResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CouponResponse of(Coupon coupon) {
        return new CouponResponse(coupon.getId(), coupon.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
