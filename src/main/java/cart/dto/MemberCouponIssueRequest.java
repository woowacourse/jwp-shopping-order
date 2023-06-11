package cart.dto;

public class MemberCouponIssueRequest {

    private Long couponId;

    public MemberCouponIssueRequest() {
    }

    public MemberCouponIssueRequest(Long couponId) {
        this.couponId = couponId;
    }

    public Long getCouponId() {
        return couponId;
    }
}
