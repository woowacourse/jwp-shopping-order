package cart.dto;

public class CouponIssueRequest {

    private final Long couponId;

    public CouponIssueRequest(final Long couponId) {
        this.couponId = couponId;
    }

    public Long getCouponId() {
        return couponId;
    }
}
