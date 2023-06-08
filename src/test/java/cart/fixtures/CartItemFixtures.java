package cart.fixtures;

import cart.domain.CartItem;

import static cart.fixtures.MemberFixtures.MEMBER1_NULL_PASSWORD;
import static cart.fixtures.MemberFixtures.MEMBER2_NULL_PASSWORD;
import static cart.fixtures.ProductFixtures.*;

public class CartItemFixtures {

    public static final CartItem CART_ITEM1 = new CartItem(1L, 2, PRODUCT1, MEMBER1_NULL_PASSWORD);
    public static final CartItem CART_ITEM2 = new CartItem(2L, 4, PRODUCT2, MEMBER1_NULL_PASSWORD);
    public static final CartItem CART_ITEM3 = new CartItem(3L, 5, PRODUCT3, MEMBER2_NULL_PASSWORD);
    public static final CartItem NEW_CART_ITEM_TO_INSERT = new CartItem(null, 1, PRODUCT3, MEMBER1_NULL_PASSWORD);
    public static final CartItem NEW_CART_ITEM = new CartItem(4L, 1, PRODUCT3, MEMBER1_NULL_PASSWORD);
    public static final CartItem UPDATE_CART_ITEM1 = new CartItem(1L, 1, PRODUCT1, MEMBER1_NULL_PASSWORD);
}
