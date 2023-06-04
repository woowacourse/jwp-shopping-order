package cart.coupon.domain;

import cart.value_object.Money;

public abstract class Coupon {

  private Long id;
  private String name;

  public Coupon(final Long id, final String name) {
    this.id = id;
    this.name = name;
  }

  public Money discount(final Money totalPrice) {
    return calculate(totalPrice);
  }

  public Money findDiscountPrice() {
    return discountPrice();
  }

  public boolean isExceedDiscountFrom(final Money totalPrice) {
    return calculate(totalPrice).isGreaterThan(totalPrice);
  }

  protected abstract Money calculate(final Money totalPrice);

  protected abstract Money discountPrice();

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
