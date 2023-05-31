package cart.domain.policy;

import cart.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

class PriceDiscountPolicyTest {
    @ParameterizedTest
    @DisplayName("가격 할인을 시행한다.")
    @CsvSource(value = {"1000,4000", "5500,0"})
    void discount(int discountValue, int resultValue) {
        Price price = new Price(5000);
        DiscountPolicy discountPolicy = new PriceDiscountPolicy(discountValue);

        assertThat(discountPolicy.discount(price)).isEqualTo(new Price(resultValue));
    }

    @Test
    @DisplayName("할인값이 0원인 경우 예외가 발생한다.")
    void exception() {
        assertThatThrownBy(() -> new PriceDiscountPolicy(0))
                .isInstanceOf(IllegalArgumentException.class);
    }

}