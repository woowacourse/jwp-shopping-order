package cart.domain.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    void createPrice_lessThanMinimumValue_throws() {
        // given
        int value = -10_000;

        // when, then
        assertThatThrownBy(() -> new Price(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 음수일 수 없습니다");
    }
}
