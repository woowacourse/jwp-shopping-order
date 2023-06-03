package cart.domain.coupon;

public class EmptyDiscount implements CouponTypes {

    @Override
    public int calculatePrice(int totalPrice, int minimumPrice, int discountPrice, double discountRate) {
        return totalPrice;
    }

    @Override
    public String getCouponTypeName() {
        return DiscountType.EMPTY_DISCOUNT.getTypeName();
    }
}


