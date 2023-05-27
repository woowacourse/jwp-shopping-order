package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cart.fixture.Fixture;

class CartItemsTest {
    @Test
    @DisplayName("주문에 포함되어 있는 제품들의 총합을 계산한다.")
    void calculatePriceSum() {
        //given
        List<CartItem> items = List.of(
                Fixture.CART_ITEM1, Fixture.CART_ITEM2
        );
        final CartItems cartItems = new CartItems(items);

        //when
        final int result = cartItems.calculatePriceSum();

        //then
        assertThat(result).isEqualTo(100000);
    }
}
