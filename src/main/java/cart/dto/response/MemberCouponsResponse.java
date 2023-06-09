package cart.dto.response;

import java.util.List;

public class MemberCouponsResponse {
    private final List<MemberCouponResponse> coupons;

    public MemberCouponsResponse(List<MemberCouponResponse> coupons) {
        this.coupons = coupons;
    }

    public List<MemberCouponResponse> getCoupons() {
        return coupons;
    }
}
