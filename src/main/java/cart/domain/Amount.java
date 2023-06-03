package cart.domain;

import java.util.Objects;

public class Amount {

  private final int value;

  public Amount(final int value) {
    this.value = value;
  }

  public Amount minus(final Amount amount) {
    return new Amount(value - amount.value);
  }

  public int getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Amount amount = (Amount) o;
    return value == amount.value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
