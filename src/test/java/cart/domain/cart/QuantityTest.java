package cart.domain.cart;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class QuantityTest {

    @Test
    void 수량은_1보다_작으면_예외를_발생한다() {
        // expect
        assertThatThrownBy(() -> new Quantity(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수량은 1이상 양수여야합니다.");
    }

    @Test
    void 수량은_1이상이면_정상_생성된다() {
        // expect
        assertDoesNotThrow(() -> new Quantity(1));
    }
}
