package cart.domain.order;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.cart.Quantity;
import cart.domain.product.ImageUrl;
import cart.domain.product.Name;
import cart.domain.product.Price;
import cart.domain.product.Product;
import java.sql.Timestamp;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderTest {
    @Test
    void 주문목록에_담긴_모든_목록의_가격을_계산한다() {
        // given
        final Quantity quantity1 = new Quantity(5);
        final Quantity quantity2 = new Quantity(10);

        final Product product1 = new Product(1L, new Name("상품1"), new ImageUrl("example.com"), new Price(1000));
        final Product product2 = new Product(2L, new Name("상품2"), new ImageUrl("example.com"), new Price(100));

        final OrderItem orderItem1 = new OrderItem(1L, 1L, quantity1, product1);
        final OrderItem orderItem2 = new OrderItem(2L, 1L, quantity2, product2);

        final Order order = new Order(
                1L,
                new Timestamp(System.currentTimeMillis()),
                List.of(orderItem1, orderItem2));

        // when
        final Price totalPrice = order.getTotalPrice();

        // then
        assertThat(totalPrice.price()).isEqualTo(6000);
    }
}
