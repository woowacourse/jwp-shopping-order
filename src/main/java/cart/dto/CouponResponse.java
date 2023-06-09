package cart.dto;

import cart.domain.Coupon;

public class CouponResponse {
    private final Long id;
    private final String name;
    private final int discountPrice;

    private CouponResponse(Long id, String name, int discountPrice) {
        this.id = id;
        this.name = name;
        this.discountPrice = discountPrice;
    }

    public static CouponResponse of(Coupon coupon) {
        return new CouponResponse(coupon.getId(), coupon.getName(), coupon.getDiscountPrice().toInt());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }
}
