package cart.domain;

import cart.exception.InvalidMoneyValueException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class MoneyTest {
    
    @Test
    void 돈은_음수가_될_수_없다() {
        assertThatThrownBy(() -> new Money(-1)).isInstanceOf(InvalidMoneyValueException.class);
    }

    @Test
    void 돈은_0이_될_수_있다() {
        assertThatCode(() -> new Money(0)).doesNotThrowAnyException();
    }
}
