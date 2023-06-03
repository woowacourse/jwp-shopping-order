package cart.domain.coupon;

import cart.exception.UnFulFilledMinimumAmountException;

public class Coupon {

    private final Long id;
    private final String couponName;
    private final int discountPercent;
    private final int discountAmount;
    private final int minAmount;

    public Coupon(final Long id, final String couponName, final Integer discountPercent, final int discountAmount, final Integer minAmount) {
        this.id = id;
        this.couponName = couponName;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.minAmount = minAmount;
    }

    public int applyDiscount(final int amount) {
        if (amount < minAmount) {
            throw new UnFulFilledMinimumAmountException("최소 주문 금액: " + minAmount + "원을 충족하지 못했습니다.");
        }
        return (amount * (100 - discountPercent) / 100) - discountAmount;
    }

    public Long getId() {
        return id;
    }

    public String getCouponName() {
        return couponName;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public Integer getMinAmount() {
        return minAmount;
    }

}
