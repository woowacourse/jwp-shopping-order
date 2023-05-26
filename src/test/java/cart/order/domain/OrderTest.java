package cart.order.domain;

import static cart.order.exception.OrderExceptionType.NON_EXIST_ORDER_ITEM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cart.common.execption.BaseExceptionType;
import cart.order.exception.OrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("Order 은(는)")
class OrderTest {

    @Test
    void 주문_상품이_최소_하나_이상_존재해야_한다() {
        // when
        BaseExceptionType baseExceptionType = assertThrows(OrderException.class, () ->
                new Order(1L)
        ).exceptionType();

        // then
        assertThat(baseExceptionType).isEqualTo(NON_EXIST_ORDER_ITEM);
    }

    @Test
    void 주문의_총액은_주문상품들의_가격의_합이다() {
        // given
        Order order = new Order(1L, 1L,
                new OrderItem(3L, 10, 1L, "말랑", 1000, "image"),  // 1000 * 10
                new OrderItem(4L, 20, 2L, "코코닥", 2000, "image")  // 2000 *  20
        );

        // when & then
        assertThat(order.totalPrice()).isEqualTo(50000);
    }
}
