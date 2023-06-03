package cart.application.dto.order;

import cart.domain.cart.CartItems;
import cart.domain.coupon.MemberCoupon;
import java.util.List;
import java.util.stream.Collectors;

public class FindOrderCouponsResponse {

    private List<FindOrderCouponResponse> coupons;

    private FindOrderCouponsResponse() {
    }

    public FindOrderCouponsResponse(final List<FindOrderCouponResponse> coupons) {
        this.coupons = coupons;
    }

    public static FindOrderCouponsResponse from(final List<MemberCoupon> memberCoupons, final CartItems cartItems) {
        List<FindOrderCouponResponse> coupons = memberCoupons.stream()
                .map(memberCoupon -> FindOrderCouponResponse.from(memberCoupon, cartItems))
                .collect(Collectors.toList());
        return new FindOrderCouponsResponse(coupons);
    }

    public List<FindOrderCouponResponse> getCoupons() {
        return coupons;
    }
}
