package cart.ui.dto.response;

public class PossibleCouponResponse {

    private Long couponId;
    private String couponName;
    private int minAmount;

    public PossibleCouponResponse() {
    }

    public PossibleCouponResponse(final Long couponId, final String couponName, final int minAmount) {
        this.couponId = couponId;
        this.couponName = couponName;
        this.minAmount = minAmount;
    }

    public Long getCouponId() {
        return couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public int getMinAmount() {
        return minAmount;
    }
}
