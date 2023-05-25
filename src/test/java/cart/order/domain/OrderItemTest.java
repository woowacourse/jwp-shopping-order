package cart.order.domain;

import static cart.order.exception.OrderExceptionType.INVALID_QUANTITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cart.common.execption.BaseExceptionType;
import cart.order.exception.OrderException;
import cart.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("OrderItem 은(는)")
class OrderItemTest {

    @Test
    void 주문_상품의_가격은_상품_단일_가격과_수량의_곱과_같다() {
        // given
        Product product = new Product("말랑", 100, "image");
        OrderItem orderItem = new OrderItem(product, 10);

        // when & then
        assertThat(orderItem.price()).isEqualTo(1000);
    }

    @Test
    void 수량은_양수여야_한다() {
        // given
        Product product = new Product("말랑", 100, "image");

        // when
        BaseExceptionType baseExceptionType = assertThrows(OrderException.class, () ->
                new OrderItem(product, 0)
        ).exceptionType();

        // then
        assertThat(baseExceptionType).isEqualTo(INVALID_QUANTITY);
    }
}
