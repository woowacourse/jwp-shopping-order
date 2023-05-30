package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderItemTest {

    @Test
    void 주문_상품이_정상적으로_생성된다() {
        Product product = new Product(1L, "사과", 1000, "http://image.com/image.png");

        OrderItem orderItem = new OrderItem(1L, product, 5, 5000);

        assertAll(
                () -> assertThat(orderItem.getId()).isEqualTo(1L),
                () -> assertThat(orderItem.getProduct()).isEqualTo(product),
                () -> assertThat(orderItem.getQuantity().getAmount()).isEqualTo(5),
                () -> assertThat(orderItem.getPrice().getAmount()).isEqualTo(5000)
        );
    }
}
