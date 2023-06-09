package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AmountTest {

  @Test
  @DisplayName("금액들의 합을 구할 수 있다.")
  void sum() {
    //given
    final List<Amount> amounts = List.of(new Amount(1000),
        new Amount(2000));

    //when
    final Amount sum = Amount.sum(amounts);

    //then
    assertThat(sum).isEqualTo(new Amount(3000));
  }

  @Test
  @DisplayName("금액을 뺄 수 있다.")
  void minus() {
    //given
    final Amount amount = new Amount(10000);
    final Amount expected = new Amount(9000);

    //when
    final Amount actual = amount.minus(new Amount(1000));

    //then
    assertThat(actual).isEqualTo(expected);
  }
}
