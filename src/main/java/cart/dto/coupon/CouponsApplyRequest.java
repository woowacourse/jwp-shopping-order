package cart.dto.coupon;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CouponsApplyRequest {

    @NotNull(message = "쿠폰을 입력해주세요. 없다면 빈 값을 보내주세요.")
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
