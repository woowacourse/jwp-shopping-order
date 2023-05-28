package cart.fixture;

import cart.domain.cart.Cart;

import static cart.fixture.CartItemsFixture.createCartItems;
import static cart.fixture.MemberFixture.createMember;

public class CartFixture {

    public static Cart createCart() {
        return new Cart(1L, createMember(), createCartItems());
    }
}
