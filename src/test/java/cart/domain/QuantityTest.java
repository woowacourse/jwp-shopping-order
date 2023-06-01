package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.NumberRangeException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class QuantityTest {

    @Test
    void 수량이_정상적으로_생성된다() {
        int amount = 100;

        Quantity quantity = new Quantity(amount);

        assertThat(quantity.getAmount()).isEqualTo(amount);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void 수량에_1보다_작은_수가_할당되면_예외가_발생한다(int amount) {
        assertThatThrownBy(() -> new Quantity(amount))
                .isInstanceOf(NumberRangeException.class)
                .hasMessage("수량은 최소 1개 이상 부터 가능합니다.");
    }
}
