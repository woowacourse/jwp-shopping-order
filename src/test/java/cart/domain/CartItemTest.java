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
        final CartItem cartItem = Fixture.CART_ITEM1;

        //when
        final int result = cartItem.calculateTotalPrice();

        //then
        assertThat(result).isEqualTo(100000);
    }
}
