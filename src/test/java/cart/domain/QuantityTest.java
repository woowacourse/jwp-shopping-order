package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;

class QuantityTest {

    @Test
    void 값이_음수가_될_수_없다() {
        // given
        int value = -1;

        // expect
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Quantity(value)
        );
    }

    @Test
    void 수량의_기본값은_1이다() {
        assertThat(Quantity.DEFAULT_VALUE).isEqualTo(1);
    }

    @Test
    void 값이_같으면_같은_객체이다() {
        // given
        Quantity quantity = new Quantity(10);
        Quantity sameQuantity = new Quantity(10);

        // expect
        assertThat(quantity).isEqualTo(sameQuantity);
        assertThat(quantity.hashCode()).isEqualTo(sameQuantity.hashCode());
    }
}
