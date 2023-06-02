package cart.domain;

import cart.domain.vo.Amount;

public class Coupon {

    private final Long id;
    private final String name;
    private final Amount discountAmount;
    private final Amount minAmount;

    public Coupon(final String name, final Amount discountAmount, final Amount minAmount) {
        this(null, name, discountAmount, minAmount);
    }

    public Coupon(final Long id, final String name, final Amount discountAmount, final Amount minAmount) {
        this.id = id;
        this.name = name;
        this.discountAmount = discountAmount;
        this.minAmount = minAmount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Amount getDiscountAmount() {
        return discountAmount;
    }

    public Amount getMinAmount() {
        return minAmount;
    }
}
