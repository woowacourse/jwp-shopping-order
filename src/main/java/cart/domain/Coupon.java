package cart.domain;

import cart.exception.UsedCouponException;

public class Coupon {

    private final Long id;
    private final int amount;
    private final DiscountPolicy discountPolicy;
    private boolean isUsed;

    public Coupon(Long id, int amount, DiscountPolicy discountPolicy) {
        this.id = id;
        this.amount = amount;
        this.discountPolicy = discountPolicy;
    }

    public Money discount(Money money) {
        validateUnused();
        this.isUsed = true;
        return discountPolicy.discount(money, amount);
    }

    private void validateUnused() {
        if (this.isUsed) {
            throw new UsedCouponException();
        }
    }

    public Long getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    public boolean isUsed() {
        return this.isUsed;
    }
}
