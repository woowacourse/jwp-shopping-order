package cart.domain.order;

import cart.domain.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderItemsTest {

    @Test
    void 모든_주문_상품의_가격의_합을_구한다() {
        // given
        final OrderItem item1 = new OrderItem(1L, "치즈피자", "치즈피자.png", new Money(500L), 3);
        final OrderItem item2 = new OrderItem(2L, "치즈피자", "치즈피자.png", new Money(100L), 3);
        final OrderItems orderItems = new OrderItems(List.of(item1, item2));

        // when
        final Money sumPrice = orderItems.sumPrice();

        // then
        assertThat(sumPrice.getValue()).isEqualTo(1800);
    }
}