package cart.domain;

import cart.exception.BusinessException;

public class Coupon {

  private final Long id;
  private final String name;
  private final Amount discountAmount;
  private final Amount minAmount;

  public Coupon(String name, Amount discountAmount, Amount minAmount) {
    this(null, name, discountAmount, minAmount);
  }

  public Coupon(Long id, String name, Amount discountAmount, Amount minAmount) {
    this.id = id;
    this.name = name;
    this.discountAmount = discountAmount;
    this.minAmount = minAmount;
  }

  public static Coupon empty() {
    return new Coupon(null, null, null, null);
  }

  public Amount apply(final Amount amount) {
    if (amount.isLessThan(minAmount)) {
      throw new BusinessException(String.format("최소 금액이 %d원 이상 이여야 합니다.", minAmount.getValue()));
    }
    return amount.minus(discountAmount);
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
