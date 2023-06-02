package cart.dto;

public class CouponResponse {

    private Long couponId;
    private String couponName;
    private int minAmount;
    private int discountAmount;
    private boolean isPublished;

    public CouponResponse() {
    }

    public CouponResponse(final Long couponId, final String couponName, final int minAmount, final int discountAmount,
        final boolean isPublished) {
        this.couponId = couponId;
        this.couponName = couponName;
        this.minAmount = minAmount;
        this.discountAmount = discountAmount;
        this.isPublished = isPublished;
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

    public int getDiscountAmount() {
        return discountAmount;
    }

    public boolean isPublished() {
        return isPublished;
    }
}
