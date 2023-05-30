package cart.domain;

import static cart.fixture.TestFixture.ORDERED_샐러드;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderItemTest {
    @Test
    void 총액을_셀_수_있다() {
        assertThat(ORDERED_샐러드.getTotal()).isEqualTo(new Money(200_000));
    }
}
