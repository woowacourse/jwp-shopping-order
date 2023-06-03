package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AmountTest {

  @Test
  void sum() {
    final Amount sum = Amount.sum(List.of(new Amount(1000),
        new Amount(2000)));

    assertThat(sum).isEqualTo(new Amount(3000));
  }

  @Test
  void minus() {
    final Amount amount = new Amount(10000);
    final Amount expected = new Amount(9000);

    final Amount actual = amount.minus(new Amount(1000));

    assertThat(actual).isEqualTo(expected);
  }
}
