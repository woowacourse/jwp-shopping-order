package cart.domain.cartitem;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class QuantityTest {

    @Test
    void 수량_생성_테스트() {
        assertDoesNotThrow(() -> Quantity.from(1));
    }

    @Test
    void 수량이_0개_이하라면_예외를_반환하다() {
        assertThatThrownBy(() -> Quantity.from(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수량은 1개 이상이어야 합니다");
    }
}
