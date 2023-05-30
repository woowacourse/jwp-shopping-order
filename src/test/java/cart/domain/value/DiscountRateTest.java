package cart.domain.value;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class DiscountRateTest {

    @ParameterizedTest(name = "{displayName}")
    @ValueSource(ints = {-1, 101, 1000})
    @DisplayName("올바르지 않은 할인률({0}%)이 들어오면 에러를 발생한다.")
    void check_discount_rate(int discountRate) {
        // when + then
        assertThatThrownBy(() -> new DiscountRate(discountRate))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
