package cart.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.InvalidMoneyException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MoneyTest {

    @ValueSource(ints = {-1, -100})
    @ParameterizedTest
    void 금액이_0보다_작으면_예외가_발생한다(int value) {
        assertThatThrownBy(() -> new Money(value))
                .isInstanceOf(InvalidMoneyException.class)
                .hasMessage("금액은 양의 정수이어야 합니다.");
    }
}
