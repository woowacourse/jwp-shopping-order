package cart.dto;

public class CouponDiscountResponse {

    private int discountedProductAmount;
    private int discountAmount;

    public CouponDiscountResponse() {
    }

    public CouponDiscountResponse(final int discountedProductAmount, final int discountAmount) {
        this.discountedProductAmount = discountedProductAmount;
        this.discountAmount = discountAmount;
    }

    public int getDiscountedProductAmount() {
        return discountedProductAmount;
    }
}
