package cart.domain;

import cart.exception.BusinessException;

public class Coupon {

  private final Amount discountAmount;
  private final Amount minAmount;

  public Coupon(Amount discountAmount, Amount minAmount) {
    this.discountAmount = discountAmount;
    this.minAmount = minAmount;
  }

  public Amount apply(final Amount amount) {
    if (amount.isLessThan(minAmount)) {
      throw new BusinessException(String.format("최소 금액이 %d이상 이여야 합니다.", minAmount.getValue()));
    }
    return amount.minus(discountAmount);
  }

  public Amount getDiscountAmount() {
    return discountAmount;
  }

  public Amount getMinAmount() {
    return minAmount;
  }
}
