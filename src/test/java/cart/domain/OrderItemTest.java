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
        Quantity quantity = new Quantity(5);
        OrderItem orderItem = new OrderItem(1L, product, quantity);

        assertAll(
                () -> assertThat(orderItem.getId()).isEqualTo(1L),
                () -> assertThat(orderItem.getProduct()).isEqualTo(product),
                () -> assertThat(orderItem.getQuantity()).isEqualTo(quantity)
        );
    }

    @Test
    void 주문_상품의_총_가격을_계산한다() {
        Product product = new Product(1L, "사과", 1000, "http://image.com/image.png");
        Quantity quantity = new Quantity(5);
        OrderItem orderItem = new OrderItem(1L, product, quantity);

        Price price = orderItem.calculatePrice();

        assertThat(price.getAmount()).isEqualTo(5000);
    }
}
