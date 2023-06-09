package cart.fixtures;

import cart.domain.CartItem;
import cart.dto.cartItem.CartItemQuantityUpdateRequest;
import cart.dto.cartItem.CartItemRequest;
import cart.dto.cartItem.CartItemResponse;

import static cart.fixtures.MemberFixtures.MEMBER1_NULL_PASSWORD;
import static cart.fixtures.MemberFixtures.MEMBER2_NULL_PASSWORD;
import static cart.fixtures.ProductFixtures.*;

public class CartItemFixtures {

    public static final CartItem CART_ITEM1 = new CartItem(1L, 2, PRODUCT1, MEMBER1_NULL_PASSWORD);
    public static final CartItem CART_ITEM2 = new CartItem(2L, 4, PRODUCT2, MEMBER1_NULL_PASSWORD);
    public static final CartItem CART_ITEM3 = new CartItem(3L, 5, PRODUCT3, MEMBER2_NULL_PASSWORD);
    public static final CartItem NEW_CART_ITEM_TO_INSERT = new CartItem(null, 1, PRODUCT3, MEMBER1_NULL_PASSWORD);
    public static final CartItem NEW_CART_ITEM = new CartItem(4L, 1, PRODUCT3, MEMBER1_NULL_PASSWORD);
    public static final CartItem UPDATE_CART_ITEM1 = new CartItem(1L, 2, PRODUCT1, MEMBER1_NULL_PASSWORD);

    public static final CartItemRequest NEW_CART_ITEM_REQUEST = new CartItemRequest(NEW_CART_ITEM.getProduct().getId());
    public static final CartItemQuantityUpdateRequest CART_ITEM_QUANTITY_UPDATE_REQUEST = new CartItemQuantityUpdateRequest(2);
    public static final CartItemQuantityUpdateRequest CART_ITEM_QUANTITY_UPDATE_REQUEST_TO_ZERO = new CartItemQuantityUpdateRequest(0);
    public static final CartItemResponse CART_ITEM_RESPONSE1 = CartItemResponse.of(CART_ITEM1);
    public static final CartItemResponse CART_ITEM_RESPONSE2 = CartItemResponse.of(CART_ITEM2);
    public static final CartItemResponse CART_ITEM_RESPONSE3 = CartItemResponse.of(CART_ITEM3);
}
