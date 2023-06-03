package cart.dto.request;

public class CouponIssueRequest {

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
