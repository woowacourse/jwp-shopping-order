package cart.dto.request;

import javax.validation.constraints.NotNull;

public class CouponIssueRequest {

    @NotNull(message = "couponId 필드가 필요합니다.")
    private Long couponId;

    public CouponIssueRequest() {
    }

    public CouponIssueRequest(Long couponId) {
        this.couponId = couponId;
    }

    public Long getCouponId() {
        return couponId;
    }
}
