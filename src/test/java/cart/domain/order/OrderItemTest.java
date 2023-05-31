package cart.domain.order;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.cart.Quantity;
import cart.domain.product.ImageUrl;
import cart.domain.product.Name;
import cart.domain.product.Price;
import cart.domain.product.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class OrderItemTest {

    @Test
    void 하나의_주문_목록은_총_금액을_계산할_수_있다() {
        // given
        final Quantity quantity = new Quantity(5);
        final Product product = new Product(1L, new Name("상품"), new ImageUrl("example.com"), new Price(1000));
        final OrderItem orderItem = new OrderItem(1L, 1L, quantity, product);

        //when
        final Price totalPrice = orderItem.getTotalPrice();

        //then
        assertThat(totalPrice.price()).isEqualTo(5000);
    }
}
