package cart.domain.value;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MoneyTest {

    @ParameterizedTest(name = "{displayName}")
    @ValueSource(ints = {-1, -100, -1000})
    @DisplayName("올바르지 않은 상품 가격({0})이면 에러를 발생한다.")
    void check_price(int price) {
        // when + then
        assertThatThrownBy(() -> new Money(price))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
