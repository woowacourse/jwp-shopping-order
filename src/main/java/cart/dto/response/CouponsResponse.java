package cart.dto.response;

import java.util.List;

public class CouponsResponse {
    private final List<CouponResponse> couponResponses;

    public CouponsResponse(List<CouponResponse> couponResponses) {
        this.couponResponses = couponResponses;
    }

    public List<CouponResponse> getCouponResponses() {
        return couponResponses;
    }
}
