package cart.domain;

public abstract class Coupon {
    public abstract Money apply(Money price);

    public abstract Money getDiscountPrice();
}
