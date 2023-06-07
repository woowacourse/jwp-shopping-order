package cart.domain;

import static cart.fixture.Fixture.CHICKEN_PRODUCT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cart.exception.DuplicatedProductCartItemException;
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

    @Test
    @DisplayName("cartItems에 포함된 product와 같은 product이면 예외를 던진다.")
    void validateContainDuplicatedProduct() {
        final CartItems cartItems = new CartItems(List.of(Fixture.CART_ITEM1, Fixture.CART_ITEM2));

        assertThatThrownBy(() -> cartItems.validateContainDuplicatedProduct(CHICKEN_PRODUCT.getId()))
                .isInstanceOf(DuplicatedProductCartItemException.class)
                .hasMessageContaining("Product is already existed in member's cart;");
    }
}
