package cart.dto.response;

public class CouponDiscountResponse {

    private final int discountedProductAmount;
    private final int discountAmount;

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
