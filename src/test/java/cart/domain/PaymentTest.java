package cart.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentTest {

    @Test
    void 지불금액은_0_이상이어야한다() {
        assertThatThrownBy(
                () -> new Payment(BigDecimal.valueOf(-5))
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("지불금액은 0 이상이어야 합니다");
    }

}