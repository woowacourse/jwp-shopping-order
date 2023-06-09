package cart.domain;

import cart.exception.BusinessException;
import java.util.Objects;

public class Coupon {

  private Long id;
  private String name;
  private Amount discountAmount;
  private Amount minAmount;

  public Coupon() {
  }

  public Coupon(String name, int discountAmount, int minAmount) {
    this(null, name, discountAmount, minAmount);
  }

  public Coupon(Long id, String name, int discountAmount, int minAmount) {
    this.id = id;
    this.name = name;
    this.discountAmount = new Amount(discountAmount);
    this.minAmount = new Amount(minAmount);
  }

  public Amount apply(final Amount amount) {
    if (amount.isLessThan(minAmount)) {
      throw new BusinessException(String.format("최소 금액이 %d원 이상 이여야 합니다.", minAmount.getValue()));
    }
    return amount.minus(discountAmount);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coupon coupon = (Coupon) o;
    return Objects.equals(getId(), coupon.getId()) && Objects.equals(getName(), coupon.getName())
        && Objects.equals(getDiscountAmount(), coupon.getDiscountAmount()) && Objects.equals(
        getMinAmount(), coupon.getMinAmount());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName(), getDiscountAmount(), getMinAmount());
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
