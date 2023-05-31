package cart.domain;

import cart.domain.vo.Amount;
import cart.exception.BusinessException;

public class AmountCoupon implements Coupon {

    private final Long id;
    private final String name;
    private final Amount discountAmount;
    private final Amount minAmount;
    private final boolean isUsed;

    public AmountCoupon(final Long id, final String name, final Amount discountAmount, final Amount minAmount,
        final boolean isUsed) {
        this.id = id;
        this.name = name;
        this.discountAmount = discountAmount;
        this.minAmount = minAmount;
        this.isUsed = isUsed;
    }

    @Override
    public Amount calculateProduct(final Amount productAmount) {
        if (productAmount.isLessThan(minAmount)) {
            throw new BusinessException("총 금액은 최소금액보다 커야합니다.");
        }
        return productAmount.minus(discountAmount);
    }

    @Override
    public Amount calculateDelivery(final Amount deliveryAmount) {
        return deliveryAmount;
    }

    @Override
    public Coupon use() {
        return new AmountCoupon(this.id, this.name, this.discountAmount, this.minAmount, this.isUsed);
    }
}
