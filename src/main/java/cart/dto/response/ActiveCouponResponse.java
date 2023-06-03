package cart.dto.response;

public class ActiveCouponResponse {

    private final Long couponId;
    private final String couponName;
    private final int minAmount;

    public ActiveCouponResponse(final Long couponId, final String couponName, final int minAmount) {
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
