package cart.value_object;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {

  public static final Money ZERO = new Money(0);

  private final BigDecimal value;

  public Money(final BigDecimal value) {
    this.value = value;
  }

  public Money(final double value) {
    this(BigDecimal.valueOf(value));
  }

  public Money add(final Money other) {
    return new Money(value.add(other.getValue()));
  }

  public Money multiply(final double multiplicand) {
    return new Money(value.multiply(BigDecimal.valueOf(multiplicand)));
  }

  public boolean isNotSame(final Money other) {
    return !this.equals(other);
  }

  public BigDecimal getValue() {
    return value;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Money money = (Money) o;
    return value.compareTo(money.value) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
