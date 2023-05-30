package cart.domain.value;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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

    @ParameterizedTest(name = "{displayName}")
    @CsvSource(value = {"0, false", "10, true"})
    @DisplayName("할인률이 0보다 크면 true를 그렇지 않으면 false를 반환한다.")
    void check_discount_true_or_false(int discount, boolean expect) {
        // given
        DiscountRate discountRate = new DiscountRate(discount);

        // when
        boolean result = discountRate.isDiscount();

        // then
        assertThat(result).isEqualTo(expect);
    }

    @ParameterizedTest
    @CsvSource(value = {"5, 0.95", "10, 0.9"})
    @DisplayName("할인된 비율을 반환한다. ex) {0}% 할인이면 결과로 {1}를 반환한다.")
    void get_discounted_rate(int discount, double expect) {
        // given
        DiscountRate discountRate = new DiscountRate(discount);

        // when
        double result = discountRate.getDiscountedPercent();

        // then
        assertThat(result).isEqualTo(expect);
    }
}
