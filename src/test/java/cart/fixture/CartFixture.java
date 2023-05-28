package cart.fixture;

import cart.domain.cart.Cart;

import static cart.fixture.CartItemsFixture.createCartItems;

public class CartFixture {

    public static Cart createCart() {
        return new Cart(1L, createCartItems());
    }
}
