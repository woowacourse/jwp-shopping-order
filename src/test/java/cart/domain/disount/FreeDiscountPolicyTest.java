package cart.domain.disount;

import cart.domain.value.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class FreeDiscountPolicyTest {

    @Test
    @DisplayName("가격 할인을 시행한다.")
    void discount() {
        Price price = new Price(5000);
        DiscountPolicy discountPolicy = new FreeDiscountPolicy();

        assertThat(discountPolicy.discount(price)).isEqualTo(new Price(0));
    }

}