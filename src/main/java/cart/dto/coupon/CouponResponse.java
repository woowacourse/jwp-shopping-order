package cart.dto.coupon;

import cart.domain.coupon.Coupon;
import cart.domain.member.MemberCoupon;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CouponResponse {

    @JsonProperty("couponId")
    private Long id;
    private String name;
    private DiscountResponse discount;

    private CouponResponse() {
    }

    private CouponResponse(final Long id, final String name, final DiscountResponse discount) {
        this.id = id;
        this.name = name;
        this.discount = discount;
    }

    public static CouponResponse from(Coupon coupon) {
        return new CouponResponse(coupon.getId(), coupon.getName(), new DiscountResponse(coupon.getDiscount()));
    }

    public static CouponResponse from(MemberCoupon coupon) {
        return new CouponResponse(coupon.getId(), coupon.getCoupon().getName(), new DiscountResponse(coupon.getCoupon().getDiscount()));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DiscountResponse getDiscount() {
        return discount;
    }
}
