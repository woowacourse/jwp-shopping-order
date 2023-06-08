package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.NumberRangeException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PriceTest {

    @Test
    void 금액이_정상적으로_생성된다() {
        long amount = 100;

        Price price = Price.from(amount);

        assertThat(price.getAmount()).isEqualTo(amount);
    }

    @Test
    void 금액에_음수가_할당되면_예외가_발생한다() {
        long amount = -1;

        assertThatThrownBy(() -> Price.from(amount))
                .isInstanceOf(NumberRangeException.class)
                .hasMessage("금액은 음수가 될 수 없습니다.");
    }

    @Test
    void 금액을_더한다() {
        Price price = Price.from(100);

        Price addPrice = price.plus(Price.from(200));

        assertThat(addPrice.getAmount()).isEqualTo(300);
    }

    @Test
    void 금액을_뺀다() {
        Price price = Price.from(300);

        Price addPrice = price.minus(Price.from(200));

        assertThat(addPrice.getAmount()).isEqualTo(100);
    }

    @Test
    void 금액을_뺐을때_음수이면_예외가_발생한다() {
        Price price = Price.from(100);

        assertThatThrownBy(() -> price.minus(Price.from(200)))
                .isInstanceOf(NumberRangeException.class)
                .hasMessage("금액은 음수가 될 수 없습니다.");
    }

    @Test
    void 금액을_곱한다() {
        Price price = Price.from(1000);

        Price multiplyPrice = price.multiply(5);

        assertThat(multiplyPrice.getAmount()).isEqualTo(5000);
    }

    @Test
    void 금액을_곱할때_음수이면_예외가_발생한다() {
        Price price = Price.from(100);

        assertThatThrownBy(() -> price.multiply(-2))
                .isInstanceOf(NumberRangeException.class)
                .hasMessage("금액은 음수가 될 수 없습니다.");
    }
}
