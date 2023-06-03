package cart.application.dto.order;

import cart.domain.cart.CartItems;
import cart.domain.coupon.MemberCoupon;
import java.util.List;
import java.util.stream.Collectors;

public class FindOrderCouponsResponse {

    private List<OrderCouponResponse> coupons;

    private FindOrderCouponsResponse() {
    }

    public FindOrderCouponsResponse(final List<OrderCouponResponse> coupons) {
        this.coupons = coupons;
    }

    public static FindOrderCouponsResponse from(final List<MemberCoupon> memberCoupons, final CartItems cartItems) {
        List<OrderCouponResponse> coupons = memberCoupons.stream()
                .map(memberCoupon -> OrderCouponResponse.from(memberCoupon, cartItems))
                .collect(Collectors.toList());
        return new FindOrderCouponsResponse(coupons);
    }

    public List<OrderCouponResponse> getCoupons() {
        return coupons;
    }
}
