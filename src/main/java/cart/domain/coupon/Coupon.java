package cart.domain.coupon;

import cart.exception.UnFulFilledMinimumAmountException;

public class Coupon {

    private final long id;
    private final String couponName;
    private final int minAmount;
    private final int discountPercent;
    private final int discountAmount;

    public Coupon(final Long id, final String couponName, final Integer minAmount, final Integer discountPercent,
                  final int discountAmount) {
        this.id = id;
        this.couponName = couponName;
        this.minAmount = minAmount;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
    }

    public int applyDiscount(final int amount) {
        if (amount < minAmount) {
            throw new UnFulFilledMinimumAmountException("최소 주문 금액: " + minAmount + "원을 충족하지 못했습니다.");
        }
        return amount * (100 - discountPercent) / 100 - discountAmount;
    }

    public Long getId() {
        return id;
    }

    public String getCouponName() {
        return couponName;
    }

    public Integer getMinAmount() {
        return minAmount;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }


}
