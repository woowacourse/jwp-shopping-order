package cart.domain.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class NameTest {

    @Test
    void 상품명은_100자_이하가_아니라면_예외를_던진다() {
        assertThatThrownBy(() -> new Name("가".repeat(101)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품명의 길이는 100자 이하여야합니다.");
    }

    @Test
    void 상품명은_100자_이하라면_예외를_던지지않는다() {
        assertDoesNotThrow(() -> new Name("가".repeat(100)));
    }
}
