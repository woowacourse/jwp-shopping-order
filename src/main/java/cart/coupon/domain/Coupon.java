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

  protected abstract Money calculate(final Money totalPrice);

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
