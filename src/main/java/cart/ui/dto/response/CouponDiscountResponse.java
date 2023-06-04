package cart.ui.dto.response;

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

    public int getDiscountAmount() {
        return discountAmount;
    }
}
