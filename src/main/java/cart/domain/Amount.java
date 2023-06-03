package cart.domain;

import cart.exception.BusinessException;
import java.util.List;
import java.util.Objects;

public class Amount {

  private final int value;

  public Amount(final int value) {
    if (value < 0) {
      throw new BusinessException("가격은 0원이상이여야 합니다.");
    }
    this.value = value;
  }

  public static Amount sum(final List<Amount> amounts) {
    return new Amount(amounts.stream()
        .mapToInt(amount -> amount.value)
        .sum());
  }

  public Amount minus(final Amount amount) {
    return new Amount(value - amount.value);
  }

  public boolean isLessThan(final Amount amount) {
    return value < amount.value;
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


  public int getValue() {
    return value;
  }
}
