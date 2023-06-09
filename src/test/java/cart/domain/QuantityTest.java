package cart.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.CartItemException;
import cart.exception.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("Quantity 단위 테스트")
class QuantityTest {
    @Test
    void 음수가_입력되면_예외를_반환한다() {
        // then
        assertThatThrownBy(() -> new Quantity(-20))
                .isInstanceOf(CartItemException.class)
                .hasMessage(ErrorMessage.INVALID_QUANTITY.getMessage());
    }
}
