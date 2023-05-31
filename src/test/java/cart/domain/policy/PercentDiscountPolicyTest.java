package cart.domain.policy;

import cart.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PercentDiscountPolicyTest {
    @Test
    @DisplayName("가격 할인을 시행한다.")
    void discount() {
        Price price = new Price(5000);
        DiscountPolicy discountPolicy = new PercentDiscountPolicy(10);

        assertThat(discountPolicy.discount(price)).isEqualTo(new Price(4500));
    }

    @Test
    @DisplayName("할인율이 0퍼센트인 경우 예외가 발생한다.")
    void exception() {
        assertThatThrownBy(() -> new PercentDiscountPolicy(0))
                .isInstanceOf(IllegalArgumentException.class);
    }

}