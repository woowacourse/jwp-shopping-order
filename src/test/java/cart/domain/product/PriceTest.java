package cart.domain.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class PriceTest {

    @Test
    void 상품_가격은_0원_이상이_아니라면_예외를_던진다() {
        assertThatThrownBy(() -> new Price(-1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 가격은 0원 이상이여야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, 1L, 100L, Long.MAX_VALUE})
    void 상품_가격은_0원_이상이라면_예외를_던지지않는다(final Long value) {
        assertDoesNotThrow(() -> new Price(value));
    }
}
