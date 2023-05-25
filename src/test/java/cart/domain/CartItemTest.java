package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cart.fixture.Fixture;

class CartItemTest {
    @Test
    @DisplayName("카트아이템이 담긴 프로덕트와 quantity를 이용해 합계를 계산한다.")
    void calculateTotalPrice() {
        //given
        final CartItem cartItem = new CartItem(1L, 10, Fixture.PIZZA_PRODUCT, Fixture.GOLD_MEMBER);

        //when
        final int result = cartItem.calculateTotalPrice();

        //then
        assertThat(result).isEqualTo(100000);
    }
}
