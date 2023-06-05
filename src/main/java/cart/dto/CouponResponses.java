package cart.dto;

import java.util.List;

public class CouponResponses {

    private final List<CouponResponse> coupons;

    public CouponResponses(List<CouponResponse> coupons) {
        this.coupons = coupons;
    }

    public List<CouponResponse> getCoupons() {
        return coupons;
    }
}
