package cart.fixture;

import cart.domain.cart.CartItem;

import static cart.fixture.ProductFixture.createProduct;
import static cart.fixture.ProductFixture.createProduct2;

public class CartItemFixture {

    public static CartItem createCartItem() {
        return new CartItem(1L, createProduct(), 1);
    }

    public static CartItem createCartItem2() {
        return new CartItem(2L, createProduct2(), 2);
    }
}
