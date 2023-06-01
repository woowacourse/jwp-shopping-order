package cart.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import cart.exception.PriceNotMatchException;
import cart.fixture.CartItemFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CartItemsTest {

    @Test
    void validateTotalPrice() {
        final CartItems cartItems = CartItems.of(List.of(CartItemFixture.CHICKEN, CartItemFixture.PIZZA));
        assertThatCode(() -> cartItems.validateTotalPrice(Price.valueOf(25_000)))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @CsvSource({"24999", "25001"})
    void validateTotalPriceFail(final int value) {
        final CartItems cartItems = CartItems.of(List.of(CartItemFixture.CHICKEN, CartItemFixture.PIZZA));
        final Price invalidPrice = Price.valueOf(value);
        assertThatThrownBy(() -> cartItems.validateTotalPrice(invalidPrice))
                .isInstanceOf(PriceNotMatchException.class)
                .hasMessage(new PriceNotMatchException(value, 25_000).getMessage());
    }
}
