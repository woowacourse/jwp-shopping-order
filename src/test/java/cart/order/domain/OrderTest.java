package cart.order.domain;

import static cart.order.exception.OrderExceptionType.NON_EXIST_ORDER_ITEM;
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
        Product product1 = new Product("말랑", 100, "image1");
        Product product2 = new Product("코코", 200, "image2");
        Product product3 = new Product("닥", 300, "image3");
        Order order = new Order(1L,
                new OrderItem(product1, 100),  // 10000
                new OrderItem(product2, 2),  // 400
                new OrderItem(product3, 3)  // 900
        );

        // when & then
        assertThat(order.totalPrice()).isEqualTo(11300);
    }
}
