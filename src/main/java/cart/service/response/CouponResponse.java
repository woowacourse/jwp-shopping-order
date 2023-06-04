package cart.service.response;

import cart.domain.coupon.Coupon;

public class CouponResponse {

    private final Long id;
    private final String name;

    private CouponResponse() {
        this(null, null);
    }

    public CouponResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static CouponResponse from(final Coupon coupon) {
        return new CouponResponse(coupon.getId(), coupon.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
