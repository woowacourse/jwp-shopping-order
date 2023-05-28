package cart.dto.coupon;

import java.util.List;

public class CouponsApplyRequest {

    private List<CouponIdRequest> coupons;

    public CouponsApplyRequest() {
    }

    public CouponsApplyRequest(final List<CouponIdRequest> coupons) {
        this.coupons = coupons;
    }

    public List<CouponIdRequest> getCoupons() {
        return coupons;
    }
}
