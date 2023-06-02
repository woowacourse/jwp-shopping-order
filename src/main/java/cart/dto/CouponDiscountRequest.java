package cart.dto;

public class CouponDiscountRequest {

    private Long couponId;
    private int totalProductAmount;

    public CouponDiscountRequest() {
    }

    public CouponDiscountRequest(final Long couponId, final int totalProductAmount) {
        this.couponId = couponId;
        this.totalProductAmount = totalProductAmount;
    }

    public Long getCouponId() {
        return couponId;
    }

    public int getTotalProductAmount() {
        return totalProductAmount;
    }
}
