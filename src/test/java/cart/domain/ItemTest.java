package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ItemTest {

    @Test
    void 상품_수량을_변경한다() {
        // given
        Product product = new Product("지구", 10000, "http://image.com");
        Item item = new Item(product);

        // when
        item.changeQuantity(100);

        // then
        assertThat(item.getQuantity()).isEqualTo(100);
    }

    @Test
    void 상품의_총_가격을_계산한다() {
        // given
        Product product = new Product("지구", 10000, "http://image.com");
        Item item = new Item(product, 10);

        // when
        Money cartPrice = item.calculateItemPrice();

        // then
        assertThat(cartPrice).isEqualTo(new Money(100000));
    }
}
