package cart.domain.value;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NameTest {

    @DisplayName("상품명이 비어있으면 예외가 발생한다.")
    @Test
    void validateEmpty() {
        // when, then
        assertThatThrownBy(() -> new Name(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비어있는 상품명은 허용되지 않습니다.");
    }

    @DisplayName("상품명이 100자를 초과하면 예외가 발생한다.")
    @Test
    void validateRange() {
        // when, then
        assertThatThrownBy(() -> new Name("1".repeat(101)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품명은 100자를 초과할 수 없습니다.");
    }
}
