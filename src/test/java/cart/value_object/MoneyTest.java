package cart.value_object;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MoneyTest {

  @Test
  @DisplayName("isGreaterThan() : 현재 숫자가 비교하는 숫자보다 더 큰 숫자인 경우 true를 반환한다.")
  void test_isGreaterThan() throws Exception {
    //given
    final Money origin = new Money(10000);
    final Money target = new Money(6000);

    //when & then
    assertTrue(origin.isGreaterThan(target));
  }
}
