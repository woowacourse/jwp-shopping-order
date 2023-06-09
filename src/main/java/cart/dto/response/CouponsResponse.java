package cart.dto.response;

import java.util.List;

public class CouponsResponse {
    private final List<CouponResponse> coupons;

    public CouponsResponse(List<CouponResponse> couponResponses) {
        this.coupons = couponResponses;
    }

    public List<CouponResponse> getCoupons() {
        return coupons;
    }
}
