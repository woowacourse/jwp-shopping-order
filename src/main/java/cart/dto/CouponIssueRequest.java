package cart.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CouponIssueRequest {

    private final long couponId;

    @JsonCreator
    public CouponIssueRequest(@JsonProperty("couponId") long couponId) {
        this.couponId = couponId;
    }

    public long getCouponId() {
        return couponId;
    }
}
