package cart.application.dto.coupon;

import cart.domain.coupon.Coupon;
import java.util.List;
import java.util.stream.Collectors;

public class FindCouponsResponse {

    private List<FindCouponResponse> coupons;

    private FindCouponsResponse() {
    }

    public FindCouponsResponse(final List<FindCouponResponse> coupons) {
        this.coupons = coupons;
    }

    public static FindCouponsResponse from(final List<Coupon> coupons) {
        return new FindCouponsResponse(
                coupons.stream()
                        .map(FindCouponResponse::from)
                        .collect(Collectors.toList())
        );
    }

    public List<FindCouponResponse> getCoupons() {
        return coupons;
    }
}
