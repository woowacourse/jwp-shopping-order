package cart.domain.order;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.VO.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderItemTest {

    @Test
    void 가격의_총합을_계산하여_반환한다() {
        // given
        final OrderItem orderItem = new OrderItem("치즈피자", "pizza.png", Money.from(8900L), 3);

        // expect
        assertThat(orderItem.calculateTotalPrice()).isEqualTo(Money.from(26700L));
    }
}
