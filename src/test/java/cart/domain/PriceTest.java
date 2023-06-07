package cart.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PriceTest {

    @Test
    void 값이_같으면_같은_객체이다() {
        // given
        Price price = new Price(1000);
        Price other = new Price(1000);

        // expect
        assertThat(price).isEqualTo(other);
    }

    @Test
    void 수량을_곱해_총_가격을_계산한다() {
        // given
        Price price = new Price(1000);
        int quantity = 10;

        // when
        Price newPrice = price.calculateTotalPrice(quantity);

        // then
        assertThat(newPrice.getValue()).isEqualTo(10000);
    }
}
