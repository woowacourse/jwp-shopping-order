package cart.value_object;

import java.math.BigDecimal;

public class Money {

  private BigDecimal value;

  private Money(final BigDecimal value) {
    this.value = value;
  }

  public Money(final double value) {
    this(BigDecimal.valueOf(value));
  }
}
