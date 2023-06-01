package cart.domain.coupon;

public interface CouponTypes {
    String getCouponTypeName();

    int calculatePrice(int totalPrice, int minimumPrice, int discountPrice, double discountRate);

}
