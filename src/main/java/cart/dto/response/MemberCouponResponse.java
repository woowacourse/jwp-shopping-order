package cart.dto.response;

import cart.domain.Coupon;
import cart.domain.MemberCoupon;

public class MemberCouponResponse {

    private Long id;
    private String name;
    private DiscountResponse discount;

    public MemberCouponResponse() {
    }

    public MemberCouponResponse(Long id, String name, DiscountResponse discount) {
        this.id = id;
        this.name = name;
        this.discount = discount;
    }

    public static MemberCouponResponse from(MemberCoupon memberCoupon) {
        Coupon coupon = memberCoupon.getCoupon();
        return new MemberCouponResponse(memberCoupon.getId(), coupon.getName(),
                new DiscountResponse(coupon.getType().name(), coupon.getDiscountAmount()));
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
