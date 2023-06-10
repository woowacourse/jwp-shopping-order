package cart.fixture;

import static cart.fixture.CartItemFixture.CART_ITEM_1;
import static cart.fixture.CartItemFixture.CART_ITEM_2;
import static cart.fixture.CartItemFixture.CART_ITEM_3;

import cart.domain.CartItems;
import java.util.List;

public class CartItemsFixture {
    public static final CartItems CART_ITEMS_1 = new CartItems(List.of(CART_ITEM_1, CART_ITEM_2, CART_ITEM_3));
}
