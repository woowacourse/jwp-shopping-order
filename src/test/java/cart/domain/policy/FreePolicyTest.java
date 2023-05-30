package cart.domain.policy;

import cart.domain.Price;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class FreePolicyTest {

    @Test
    @DisplayName("가격 할인을 시행한다.")
    void discount() {
        Price price = new Price(5000);
        DiscountPolicy discountPolicy = new FreePolicy();

        assertThat(discountPolicy.discount(price)).isEqualTo(new Price(0));
    }

}