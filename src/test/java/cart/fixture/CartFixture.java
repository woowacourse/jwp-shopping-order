package cart.fixture;

import cart.domain.cart.Cart;

import static cart.fixture.CartItemsFixture.createCartItems;
import static cart.fixture.CartItemsFixture.createSimpleCartItems;

public class CartFixture {

    public static Cart createCart() {
        return new Cart(1L, createCartItems());
    }

    public static Cart createSimpleCart() {
        return new Cart(1L, createSimpleCartItems());
    }
}