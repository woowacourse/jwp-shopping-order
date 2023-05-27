package cart.fixture;

import cart.domain.cart.CartItem;
import cart.domain.cart.CartItems;

import java.util.ArrayList;
import java.util.List;

import static cart.fixture.CartItemFixture.createCartItem;
import static cart.fixture.CartItemFixture.createCartItem2;

public class CartItemsFixture {

    public static CartItems createCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(createCartItem());
        cartItems.add(createCartItem2());

        return new CartItems(cartItems);
    }
}
