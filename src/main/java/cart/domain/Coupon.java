package cart.domain;

public class Coupon {

  private final Amount discountAmount;
  private final Amount minAmount;

  public Coupon(Amount discountAmount, Amount minAmount) {
    this.discountAmount = discountAmount;
    this.minAmount = minAmount;
  }

  public Amount apply(final Amount amount) {
    return amount.minus(discountAmount);
  }

  public Amount getDiscountAmount() {
    return discountAmount;
  }

  public Amount getMinAmount() {
    return minAmount;
  }
}
