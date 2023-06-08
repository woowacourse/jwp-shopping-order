package cart.domain;

import cart.domain.vo.Amount;
import cart.exception.BusinessException;

public class Coupon {

    private final Long id;
    private final String name;
    private final Amount discountAmount;
    private final Amount minAmount;
    private final boolean isUsed;

    public Coupon(final String name, final Amount discountAmount, final Amount minAmount, final boolean isUsed) {
        this(null, name, discountAmount, minAmount, isUsed);
    }

    public Coupon(final Long id, final String name, final Amount discountAmount, final Amount minAmount,
        final boolean isUsed) {
        this.id = id;
        this.name = name;
        this.discountAmount = discountAmount;
        this.minAmount = minAmount;
        this.isUsed = isUsed;
    }

    public Amount calculateProduct(final Amount productAmount) {
        if (isUsed) {
            throw new BusinessException("이미 사용한 쿠폰입니다.");
        }
        if (productAmount.isLessThan(minAmount)) {
            throw new BusinessException("총 금액은 최소금액보다 커야합니다.");
        }
        return productAmount.minus(discountAmount);
    }

    public Coupon use() {
        return new Coupon(this.id, this.name, this.discountAmount, this.minAmount, true);
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

    public boolean isUsed() {
        return isUsed;
    }
}
