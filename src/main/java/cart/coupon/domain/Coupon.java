package cart.coupon.domain;

import cart.value_object.Money;

public abstract class Coupon {

  protected static final String NOT_USE_COUPON_NAME = "쿠폰 미사용";
  protected static final Long NOT_USE_ID = 0L;

  private final Long id;
  private final String name;

  protected Coupon(final Long id, final String name) {
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

  public boolean isExisted() {
    return id != null && !id.equals(NOT_USE_ID);
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
