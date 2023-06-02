package cart.dto;

public class CouponDiscountResponse {

    private int discountedProductAmount;

    public CouponDiscountResponse() {
    }

    public CouponDiscountResponse(final int discountedProductAmount) {
        this.discountedProductAmount = discountedProductAmount;
    }

    public int getDiscountedProductAmount() {
        return discountedProductAmount;
    }
}
