package cart.dto;

import cart.domain.Coupon;

public class CouponResponse {
    private final Long id;
    private final String name;
    private final int discountPercent;

    public CouponResponse(Long id, String name, int discountPercent) {
        this.id = id;
        this.name = name;
        this.discountPercent = discountPercent;
    }

    public static CouponResponse of(Coupon coupon) {
        return new CouponResponse(coupon.getId(), coupon.getName(), coupon.getDiscountPercent().getValue());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }
}
