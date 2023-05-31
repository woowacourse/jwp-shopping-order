package cart.coupon.domain;

public interface DiscountPolicy {

    int calculatePrice(int price);

    int getValue();

    DiscountType getDiscountType();
}
